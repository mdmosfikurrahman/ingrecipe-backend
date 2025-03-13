CREATE TABLE rating
(
    id        SERIAL PRIMARY KEY,
    recipe_id BIGINT           NOT NULL,
    user_id   BIGINT           NOT NULL,
    rating    DOUBLE PRECISION NOT NULL CHECK (rating >= 0 AND rating <= 5),
    CONSTRAINT fk_rating_recipe FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE CASCADE,
    CONSTRAINT unique_recipe_user UNIQUE (recipe_id, user_id)
);