CREATE TABLE comment
(
    id        SERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL,
    user_id   BIGINT NOT NULL,
    content   TEXT,
    CONSTRAINT fk_comment_recipe FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE CASCADE
);