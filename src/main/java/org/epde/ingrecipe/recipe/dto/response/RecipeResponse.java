package org.epde.ingrecipe.recipe.dto.response;

import lombok.Data;
import org.epde.ingrecipe.recipe.model.Recipe;
import org.epde.ingrecipe.recipe.model.RecipeIngredient;

import java.util.List;

@Data
public class RecipeResponse {
    private Long id;
    private String title;
    private String description;
    private List<String> ingredients;
    private String instructions;
    private double rating;
    private List<CommentResponse> comments;

    public RecipeResponse(Recipe recipe, List<CommentResponse> comments) {
        this.id = recipe.getId();
        this.title = recipe.getTitle();
        this.description = recipe.getDescription();
        this.ingredients = recipe.getIngredients().stream()
                .map(RecipeIngredient::getIngredient)
                .toList();
        this.instructions = recipe.getInstructions();
        this.rating = recipe.getRatings();
        this.comments = comments;
    }
}
