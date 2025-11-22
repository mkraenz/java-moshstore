CREATE TABLE carts
(
    id           BINARY(16) DEFAULT (Uuid_to_bin(Uuid())) NOT NULL,
    date_created DATE       DEFAULT (CURDATE())           NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE cart_items
(
    id         BIGINT auto_increment,
    quantity   INT        NOT NULL,
    cart_id    BINARY(16) NOT NULL,
    product_id BIGINT     NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

ALTER TABLE cart_items
    ADD CONSTRAINT cart_items_products_id_fk FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE;

ALTER TABLE cart_items
    ADD CONSTRAINT cart_items_carts_id_fk FOREIGN KEY (cart_id) REFERENCES carts (id) ON DELETE CASCADE;
