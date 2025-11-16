ALTER TABLE wishlist
    DROP
        FOREIGN KEY fk_wishlist_on_product;


ALTER TABLE wishlist
    ADD CONSTRAINT fk_wishlist_on_product
        FOREIGN KEY (product_id) REFERENCES products (id) ON
            DELETE CASCADE;