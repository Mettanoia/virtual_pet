CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL
);


CREATE TABLE pets
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    master_id      BIGINT       NOT NULL,
    name           VARCHAR(100) NOT NULL,
    pet_type       VARCHAR(50)  NOT NULL,
    energy         VARCHAR(50)  NOT NULL,
    mood           VARCHAR(50)  NOT NULL,
    pet_level      VARCHAR(50)  NOT NULL,
    last_feed_time BIGINT       NOT NULL,
    CONSTRAINT fk_master FOREIGN KEY (master_id) REFERENCES users (id)
);
