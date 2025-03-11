CREATE TABLE token_blacklist
(
    id             BIGSERIAL PRIMARY KEY,
    token          TEXT      NOT NULL UNIQUE,
    user_id        BIGINT    NOT NULL,
    invalidated_at TIMESTAMP,
    expires_at     TIMESTAMP NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);