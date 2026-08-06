INSERT INTO users(email, username, role, password_hash)
VALUES ('admin@mail.com', 'admin', 'ADMIN', '$2a$10$akn5QXa9xX/GPHcyOHijPuLea4J7WiJZXgXkAXYwcQlIvLZn6P3Ie'),
       ('user@mail.com', 'user', 'CUSTOMER', '$2a$10$akn5QXa9xX/GPHcyOHijPuLea4J7WiJZXgXkAXYwcQlIvLZn6P3Ie');

INSERT INTO categories(name, slug)
VALUES ('Phone', 'Phone'),
       ('Laptop', 'Laptop'),
       ('Shirt', 'Shirt');

INSERT INTO products(name, price, stock_quantity)
VALUES ('T-Shirt', 320000, 10),
       ('Hoodie', 550000, 32),
       ('iphone 17 Pro Max', 33000000, 36);
