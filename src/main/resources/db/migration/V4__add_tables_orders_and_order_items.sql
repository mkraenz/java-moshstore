CREATE TABLE orders
(
    id          BIGINT auto_increment              NOT NULL,
    status      VARCHAR(20)                        NOT NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    total_price DECIMAL(10, 2)                     NOT NULL,
    customer_id BIGINT                             NULL,
    CONSTRAINT `primary` PRIMARY KEY (id),
    CONSTRAINT orders_users_id_fk FOREIGN KEY (customer_id) REFERENCES users (
                                                                              id) ON DELETE SET NULL
);

CREATE TABLE order_items
(
    id          BIGINT auto_increment NOT NULL,
    order_id    BIGINT                NOT NULL,
    product_id  BIGINT                NOT NULL,
    unit_price  DECIMAL(10, 2)        NOT NULL,
    quantity    INT                   NOT NULL,
    total_price DECIMAL(10, 2)        NOT NULL,
    CONSTRAINT `primary` PRIMARY KEY (id),
    CONSTRAINT order_items_orders_id_fk FOREIGN KEY (order_id) REFERENCES
        orders (id) ON DELETE CASCADE,
    CONSTRAINT order_items_products_id_fk FOREIGN KEY (product_id) REFERENCES
        products (id)
)