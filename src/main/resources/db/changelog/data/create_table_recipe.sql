CREATE TABLE recipe
(
    id           SERIAL PRIMARY KEY,
    title        TEXT NOT NULL,
    description  TEXT,
    instructions TEXT,
    ratings      DOUBLE PRECISION DEFAULT 0.0,
    status       BIGINT       NOT NULL
);