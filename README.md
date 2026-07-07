# MiniShop API

Backend e-commerce REST API — project học Spring Boot (portfolio).

> Mục tiêu: chạm vào đúng những "điểm đau" backend mà phỏng vấn viên hay đào:
> transaction khi đặt hàng, chống oversell bằng optimistic locking, JWT auth,
> DTO mapping, validation, global exception handling, pagination/filter.

## Tech stack

- Spring Boot 3.3.x / Java 17
- Spring Data JPA + PostgreSQL
- Flyway (quản lý schema bằng migration)
- Spring Security + JWT (jjwt)
- MapStruct (entity ↔ DTO)
- springdoc-openapi (Swagger UI)
- Testcontainers (integration test với Postgres thật)

## Kiến trúc

Tổ chức theo **package-by-feature** (mỗi domain một package, gom controller/service/
repository/entity/dto lại cùng chỗ) thay vì package-by-layer. Dễ điều hướng, dễ tách
module sau này — và là một điểm để nói khi phỏng vấn.

```
dev.dada.minishop
├── config        # Security, OpenAPI, JPA auditing
├── common        # BaseEntity, ApiResponse, PageResponse
├── security      # JwtService, filter, UserDetailsService
├── exception     # GlobalExceptionHandler + custom exceptions
├── user          # User, auth (register/login)
├── category
├── product
├── cart
├── order         # <-- trái tim: @Transactional + optimistic locking
└── payment       # mock, không tích hợp cổng thật
```

## Chạy local

1. Khởi động Postgres:
   ```bash
   docker compose up -d
   ```
2. Copy `.env.example` → `.env` và điền giá trị (đặc biệt `JWT_SECRET`).
3. Sinh Maven wrapper (chạy 1 lần, cần Maven cài sẵn):
   ```bash
   mvn -N wrapper:wrapper
   ```
4. Chạy app:
   ```bash
   ./mvnw spring-boot:run
   ```
5. Swagger UI: http://localhost:8080/swagger-ui.html

## Quy trình làm việc đề xuất

Làm theo `TASKS.md` — backlog đã chia ticket như sprint thật, có thứ tự phụ thuộc.
Mỗi ticket có ID (vd `MS-18`); trong code các file stub đã ghi sẵn `TODO MS-xx` trỏ về
ticket tương ứng, nên bạn luôn biết file nào thuộc task nào.

## Ghi chú phỏng vấn (điền dần khi làm)

Mỗi khi gặp vấn đề khó, ghi lại vào đây: vấn đề gì, vì sao, giải quyết ra sao.
Đây chính là nguyên liệu kể chuyện trung thực khi phỏng vấn.

- _(ví dụ)_ Khi làm `placeOrder`, ban đầu chưa nghĩ tới 2 người mua cùng lúc → ...
