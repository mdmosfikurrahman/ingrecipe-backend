package org.epde.ingrecipe.recipe.repository;

import org.epde.ingrecipe.recipe.enums.RecipeStatus;
import org.epde.ingrecipe.recipe.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByTitleContainingIgnoreCaseAndStatus(String title, RecipeStatus status);

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.ingredients WHERE r.status = :status")
    List<Recipe> findByStatusWithIngredients(@Param("status") RecipeStatus status);

}
