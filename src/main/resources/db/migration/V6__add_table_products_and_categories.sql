CREATE TABLE categories
(
    id   TINYINT auto_increment NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE products
(
    id          BIGINT auto_increment NOT NULL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    category_id TINYINT NOT NULL,
    CONSTRAINT products_categories_id_fk FOREIGN KEY (category_id) REFERENCES
        categories (id) ON DELETE RESTRICT
);