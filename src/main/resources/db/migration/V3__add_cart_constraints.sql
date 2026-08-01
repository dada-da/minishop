ALTER TABLE carts
    ADD CONSTRAINT uq_user_cart
        UNIQUE (user_id);

ALTER TABLE cart_items
    ADD CONSTRAINT uq_product_cart
        UNIQUE (product_id, cart_id);