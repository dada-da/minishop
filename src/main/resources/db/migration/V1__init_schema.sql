-- TASK MS-02: schema khoi tao.
-- Viet DDL cho cac bang: users, categories, products, carts, cart_items,
-- orders, order_items, payments.
-- Nho: products.version (BIGINT) cho optimistic locking (MS-21).
-- Day la file Flyway dau tien, chay tu dong khi khoi dong app.

CREATE TABLE users
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username      VARCHAR(50),
    full_name     VARCHAR(100),
    email         VARCHAR(100) NOT NULL,
    password_hash TEXT,
    role          VARCHAR(50)  NOT NULL DEFAULT 'USER',
    active        BOOLEAN               DEFAULT TRUE,
    created_at    TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ
);

CREATE TABLE categories
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       TEXT         NOT NULL,
    slug       VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT references users (id),
    updated_at TIMESTAMPTZ
);

CREATE TABLE products
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(250)   NOT NULL,
    description    TEXT,
    price          NUMERIC(12, 0) NOT NULL,
    original_price NUMERIC(12, 0),
    stock_quantity INTEGER                 DEFAULT 0,
    version        BIGINT         NOT NULL DEFAULT 1,
    category_id    BIGINT,
    created_at     TIMESTAMPTZ             DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ
);

CREATE TABLE carts
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    BIGINT references users (id),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);

CREATE TABLE cart_items
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cart_id    BIGINT references carts (id),
    product_id BIGINT references products (id),
    quantity   INTEGER NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);

CREATE TABLE orders
(
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id               BIGINT references users (id) NOT NULL,
    status                VARCHAR(30) DEFAULT 'PENDING',
    total_amount          NUMERIC(12, 0)               NOT NULL,
    total_original_amount NUMERIC(12, 0),
    created_at            TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMPTZ
);

CREATE TABLE order_items
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id       BIGINT references orders (id)   NOT NULL,
    product_id     BIGINT references products (id) NOT NULL,
    product_name   VARCHAR(250)                    NOT NULL,
    quantity       INTEGER                         NOT NULL,
    price          NUMERIC(12, 0)                  NOT NULL,
    original_price NUMERIC(12, 0),
    created_at     TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ
);

CREATE TABLE payments
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id        BIGINT references orders (id) NOT NULL,
    transaction_id  VARCHAR(250),
    status          VARCHAR(30) DEFAULT 'PENDING',
    amount          NUMERIC(12, 0)                NOT NULL,
    idempotency_key VARCHAR(43)                   NOT NULL UNIQUE,
    method          VARCHAR(30),
    created_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ
);

