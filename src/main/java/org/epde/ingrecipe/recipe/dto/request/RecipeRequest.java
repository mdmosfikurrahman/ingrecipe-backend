package org.epde.ingrecipe.recipe.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RecipeRequest {
    private String title;
    private String description;
    private List<String> ingredients;
    private String instructions;
}
