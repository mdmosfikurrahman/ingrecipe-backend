package org.epde.ingrecipe.recipe.repository;

import org.epde.ingrecipe.recipe.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Rating r WHERE r.recipeId = :recipeId")
    double calculateAverageRating(@Param("recipeId") Long recipeId);

    Optional<Rating> findByRecipeIdAndUserId(Long recipeId, Long userId);

}
