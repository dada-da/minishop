# MiniShop API — Backlog (chia task như công việc thật)

Mỗi ticket có: **ID**, mô tả, *Acceptance Criteria* (định nghĩa "xong"), ước lượng
(điểm story / giờ), và **phụ thuộc**. Làm tuần tự theo thứ tự sprint; trong code các
file đã đánh dấu `TODO MS-xx` khớp với ID dưới đây.

Quy ước estimate: ◔ ≈ 1-2h · ◑ ≈ nửa ngày · ◕ ≈ 1 ngày · ● ≈ 2+ ngày

---

## EPIC A — Nền tảng & Setup

### MS-01 · Khởi tạo project + chạy được "hello"  ◔
- Import vào IDE, `./mvnw spring-boot:run` lên được, kết nối Postgres OK.
- **AC:** app start không lỗi; Flyway tạo bảng `flyway_schema_history`.
- Phụ thuộc: —

### MS-02 · BaseEntity + JPA Auditing + schema V1  ◑
- Hoàn thiện `BaseEntity` (id, createdAt, updatedAt). Viết DDL `V1__init_schema.sql`.
- **AC:** `ddl-auto: validate` pass (schema khớp entity); cột created/updated tự điền.
- Phụ thuộc: MS-01

### MS-03 · Wrapper response chung (tùy chọn)  ◔
- `ApiResponse<T>` + factory `ok()/error()`.
- **AC:** ít nhất 1 endpoint trả về đúng format `{success, message, data}`.
- Phụ thuộc: MS-01

---

## EPIC B — Catalog (Category + Product)

### MS-04 · Entity Category  ◔
- **AC:** map đúng quan hệ 1-N với Product; có trong V1 migration.
- Phụ thuộc: MS-02

### MS-05 · CRUD Category API  ◑
- GET công khai; POST/PUT/DELETE chỉ ADMIN (gắn sau khi có security).
- **AC:** CRUD chạy qua Swagger; trả DTO, không trả entity.
- Phụ thuộc: MS-04

### MS-06 · Entity Product (+ field version)  ◑
- price `BigDecimal`, stockQuantity `int`, `@Version Long`, `@ManyToOne` Category.
- **AC:** lưu/đọc được; cột `version` tồn tại trong DB.
- Phụ thuộc: MS-04

### MS-07 · CRUD Product API + DTO/MapStruct  ◕
- Mapper Product↔DTO; validation trên `ProductRequest`.
- **AC:** tạo product với input sai (giá âm) bị 400; output luôn là DTO.
- Phụ thuộc: MS-06

### MS-08 · Seed data  ◔
- `V2__seed_data.sql`: 1 admin, vài category, vài product.
- **AC:** sau khi start có sẵn dữ liệu để test.
- Phụ thuộc: MS-06

---

## EPIC C — Authentication & Authorization

### MS-10 · Entity User + Role + repository  ◑
- email unique, passwordHash, role enum.
- **AC:** `findByEmail`, `existsByEmail` chạy đúng.
- Phụ thuộc: MS-02

### MS-11 · SecurityConfig (filter chain, password encoder)  ◕
- Stateless, phân quyền public/customer/admin, BCrypt.
- **AC:** endpoint admin trả 401/403 khi chưa đăng nhập.
- Phụ thuộc: MS-10

### MS-12 · JWT (service + filter + UserDetailsService)  ●
- Sinh/validate access & refresh token; đọc secret từ config.
- **AC:** token hợp lệ qua được filter; token sai → 401.
- Phụ thuộc: MS-11

### MS-13 · API register / login / refresh  ◕
- Hash password, chặn email trùng, trả token.
- **AC:** đăng ký → đăng nhập → gọi được endpoint cần auth bằng token nhận về.
- Phụ thuộc: MS-12

---

## EPIC D — Cart

### MS-14 · Entity Cart + CartItem  ◑
- Cart `@OneToOne` User; CartItem `@ManyToOne` Cart & Product.
- **AC:** cascade lưu item đúng; mỗi user 1 cart.
- Phụ thuộc: MS-06, MS-10

### MS-15 · Cart API (add/update/remove/get/clear)  ◕
- Lấy user từ SecurityContext — chỉ thao tác cart của chính mình.
- **AC:** user A không xem/sửa được cart user B; tính total đúng.
- Phụ thuộc: MS-14, MS-13

---

## EPIC E — Order & Payment (phần lõi)

### MS-17 · Entity Order + OrderItem + status  ◕
- OrderItem **snapshot** giá & tên tại thời điểm đặt (không tham chiếu giá product hiện tại).
- **AC:** đổi giá product sau khi đặt không làm đổi tổng đơn cũ.
- Phụ thuộc: MS-06, MS-10

### MS-18 · placeOrder() trong @Transactional  ●  ⭐
- Trừ kho + tạo order + tạo payment trong **một** transaction; lỗi → rollback hết.
- **AC:** ép payment fail → kho không bị trừ, không có order rác.
- Phụ thuộc: MS-15, MS-17, MS-20

### MS-19 · Order API (checkout, lịch sử, chi tiết, đổi status)  ◕
- POST checkout; GET lịch sử (phân trang) của user; admin PATCH status.
- **AC:** user chỉ thấy đơn của mình; admin đổi được trạng thái.
- Phụ thuộc: MS-18

### MS-20 · Payment (mock)  ◑
- createPayment(PENDING); confirmPayment → SUCCESS/FAILED; SUCCESS → Order.PAID.
- **AC:** confirm SUCCESS cập nhật đúng status đơn.
- Phụ thuộc: MS-17

### MS-21 · Chống oversell — optimistic locking  ●  ⭐
- Dùng `@Version` trên Product; bắt `OptimisticLockException` khi trừ kho.
- **AC:** xem test MS-28 (2 luồng mua món cuối → chỉ 1 thành công).
- Phụ thuộc: MS-18

---

## EPIC F — Chất lượng & hoàn thiện (phần "ăn điểm")

### MS-24 · GlobalExceptionHandler  ◕
- Map mọi exception về `ErrorResponse` đồng nhất (404/409/400/403/500).
- **AC:** mọi lỗi trả JSON cùng cấu trúc; validation trả danh sách field lỗi.
- Phụ thuộc: MS-07

### MS-25 · Pagination + filter product  ◕
- `Pageable` + `Specification` (filter category, khoảng giá, keyword). Trả `PageResponse`.
- **AC:** `?page=0&size=10&category=...&minPrice=...` hoạt động đúng.
- → *Talking point*: nối với kinh nghiệm tối ưu collection page ở Solis.
- Phụ thuộc: MS-07

### MS-26 · Test ProductController (@WebMvcTest)  ◑
- Test phân trang, filter, phân quyền.
- Phụ thuộc: MS-25

### MS-27 · Integration test placeOrder (Testcontainers)  ◕  ⭐
- Case: thành công / cart rỗng / thiếu kho (kiểm tra rollback).
- Phụ thuộc: MS-18

### MS-28 · Concurrency test (oversell)  ◕  ⭐
- 2 luồng mua món cuối → chỉ 1 thành công, 1 nhận lỗi hết hàng.
- Phụ thuộc: MS-21

### MS-30 · Swagger/OpenAPI config + bearer auth  ◔
- Mô tả API, nút Authorize nhập JWT.
- Phụ thuộc: MS-13

### MS-31 · Deploy lên Render + UptimeRobot  ◑
- Biến môi trường (DB Neon, JWT secret); ping giữ app khỏi sleep.
- Phụ thuộc: hầu hết các epic xong.

---

## Gợi ý chia sprint (5 tuần, tốc độ "vừa làm vừa nghiên cứu")

| Tuần | Tickets | Trọng tâm |
|------|---------|-----------|
| 1 | MS-01 → MS-08 | Setup, catalog chạy được |
| 2 | MS-10 → MS-13 | Auth/JWT (khó nhất) |
| 3 | MS-14, MS-15, MS-17, MS-20 | Cart + chuẩn bị order |
| 4 | MS-18, MS-19, MS-21 | Lõi: transaction + chống oversell |
| 5 | MS-24 → MS-31 | Test, exception, docs, deploy |

⭐ = ticket có giá trị phỏng vấn cao nhất. Nếu thiếu thời gian, ưu tiên làm cho
xong nhóm ⭐ hơn là thêm tính năng mới.
