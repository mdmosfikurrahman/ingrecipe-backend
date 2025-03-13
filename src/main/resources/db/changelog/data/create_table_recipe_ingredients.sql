CREATE TABLE recipe_ingredients
(
    id          BIGSERIAL PRIMARY KEY,
    recipe_id   BIGINT NOT NULL,
    ingredient  TEXT NOT NULL,
    CONSTRAINT fk_recipe_id FOREIGN KEY (recipe_id)
        REFERENCES recipe(id) ON DELETE CASCADE
);
