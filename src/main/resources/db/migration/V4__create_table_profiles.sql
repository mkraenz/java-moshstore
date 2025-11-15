CREATE TABLE profiles
(
    id             BIGINT NOT NULL PRIMARY KEY,
    bio            TEXT NULL,
    phone_number   VARCHAR(15) NULL,
    date_of_birth  DATE NULL,
    loyalty_points INT UNSIGNED DEFAULT 0 NOT NULL,
    CONSTRAINT profiles_users_id_fk FOREIGN KEY (id) REFERENCES users (id)
);