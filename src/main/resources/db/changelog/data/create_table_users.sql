CREATE TABLE users
(
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(255) NOT NULL UNIQUE,
    email           VARCHAR(255) NOT NULL UNIQUE,
    actual_password VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    role            INTEGER      NOT NULL CHECK (role BETWEEN 1 AND 3),
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP    NOT NULL,
    updated_at      TIMESTAMP    NOT NULL,
    last_login      TIMESTAMP NULL
);
