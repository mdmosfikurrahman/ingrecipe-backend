package org.epde.ingrecipe.recipe.service;

import org.epde.ingrecipe.recipe.dto.request.RecipeRequest;
import org.epde.ingrecipe.recipe.dto.response.RecipeResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface RecipeService {

    List<RecipeResponse> viewApprovedRecipes();

    List<RecipeResponse> viewUnapprovedRecipes();

    RecipeResponse addRecipe(RecipeRequest request);

    List<RecipeResponse> searchRecipes(String query);

    RecipeResponse editRecipe(Long recipeId, RecipeRequest request);

    void deleteRecipe(Long recipeId);

    void moderateRecipe(Long recipeId, Long actionId);

    void rateRecipe(Long recipeId, double rating, String authHeader, Authentication authentication);

}
