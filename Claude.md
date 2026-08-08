# CLAUDE.md — MiniShop

Hướng dẫn này áp dụng cho mọi session Claude Code trong repo này, bao gồm cả
`/code-review`. Claude nên đọc và tuân theo trước khi đề xuất hoặc chỉnh sửa
bất kỳ code nào.

## 1. Tổng quan project

- **Tên:** MiniShop — REST API portfolio project, mục tiêu chứng minh năng lực
  backend Java/Spring Boot cho các buổi phỏng vấn.
- **Stack:** Java 21, Spring Boot 3, Spring Security (JWT), PostgreSQL, Flyway,
  Docker.
- **Package gốc:** `dev.dada.minishop`
- **Định hướng:** ưu tiên code thể hiện rõ tư duy kiến trúc backend đúng chuẩn
  hơn là chạy được nhanh — vì đây là portfolio, người review (nhà tuyển dụng)
  sẽ đọc code kỹ.

## 2. Nguyên tắc kiến trúc bắt buộc

### Thin Controller, Fat Service
- Controller **chỉ** làm: nhận request, validate input (`@Valid`), gọi đúng
  một method của service, trả response. Không chứa business logic, không
  chứa điều kiện rẽ nhánh nghiệp vụ.
- Toàn bộ business logic thuộc về Service layer.
- Nếu thấy Controller có `if/else` xử lý nghiệp vụ (không phải xử lý
  HTTP/validation), đây là vi phạm cần được gắn cờ.

### REST semantics nghiêm ngặt
- Mỗi HTTP method có đúng một trách nhiệm: `PUT`/`PATCH` để update,
  `DELETE` để xóa. Không dùng `PUT` để ẩn ý xóa (ví dụ set quantity=0 hoặc âm
  để trigger delete) nếu đã có endpoint `DELETE` riêng — tạo ra hai đường xử
  lý cùng một hành vi, dễ gây dead code và khó bảo trì.
- Method service nên có **một trách nhiệm rõ ràng duy nhất**. Tránh một
  method (ví dụ `updateCartItem`) nội bộ gọi method khác cùng cấp trách
  nhiệm (ví dụ `deleteCartItem`) trừ khi có yêu cầu nghiệp vụ tường minh
  — và khi đó, xử lý logic đó tại chỗ, có comment giải thích, thay vì
  delegate ngầm.

### DTO & Validation
- Input luôn qua DTO có annotation validation (`@Valid`, `@Min`, `@NotNull`,
  ...) ở tầng Controller. Không validate thủ công bằng if trong service nếu
  Bean Validation đã đủ.

### Bảo mật
- Không log dữ liệu nhạy cảm (JWT, password, PII) ra console/log file.
- Query liên quan đến user data phải scope theo `@AuthenticationPrincipal`
  hoặc userId tương ứng — tuyệt đối không truy vấn xuyên user nếu không có
  kiểm tra quyền.

## 3. Cách Claude review code trong project này

**Nguyên tắc quan trọng nhất: không tự sửa code hộ.**

Khi chạy `/code-review` (không truyền `--fix`):
- Chỉ **phân tích** và **chỉ ra vấn đề**, không tự động chỉnh code.
- Với mỗi finding, giải thích:
    1. Vấn đề là gì, nằm ở đâu (file:line).
    2. **Tại sao** đây là vấn đề — nêu nguyên lý/kiến thức liên quan (ví dụ:
       "vi phạm Single Responsibility vì...", "đây là race condition vì...",
       trích dẫn pattern hoặc best practice cụ thể nếu có).
    3. Gợi ý hướng sửa ở mức khái niệm (pseudo-code hoặc mô tả cách tiếp cận),
       **không** viết sẵn đoạn code hoàn chỉnh để paste vào — mục tiêu là để
       người dùng tự tay code lại, không phải để Claude làm thay.
- Nếu người dùng muốn Claude tự sửa, họ sẽ chủ động gọi `--fix` hoặc yêu cầu
  rõ ràng trong hội thoại. Mặc định luôn ưu tiên dạy, không ưu tiên tốc độ.

Mục tiêu: đây là project để luyện tư duy và chuẩn bị phỏng vấn backend, nên
giá trị nằm ở việc hiểu tại sao — không phải ở việc có code chạy được nhanh
nhất.

## 4. Không cần gắn cờ

- Style/format đã có linter/formatter xử lý riêng — không cần lặp lại nhận
  xét về indentation, dấu chấm phẩy, import order.
- Thiếu test coverage — sẽ được xử lý ở giai đoạn riêng, không phải trọng
  tâm của review kiến trúc.