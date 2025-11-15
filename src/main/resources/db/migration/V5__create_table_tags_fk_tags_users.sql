CREATE TABLE tags
(
    id   BIGINT NOT NULL auto_increment PRIMARY KEY,
    name VARCHAR(255) NULL
);

CREATE TABLE users_tags
(
    user_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    PRIMARY KEY (user_id, tag_id),
    CONSTRAINT users_tags_tags_id_fk FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE,
    CONSTRAINT users_tags_users_id_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);