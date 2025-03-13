package org.epde.ingrecipe.recipe.validator;

import org.epde.ingrecipe.common.exception.ValidationException;
import org.epde.ingrecipe.recipe.dto.request.RecipeRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component
public class RecipeValidator {

    public void validate(RecipeRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request == null) {
            errors.put("request", "Recipe request must not be null");
        } else {
            validateTitle(request.getTitle(), errors);
            validateDescription(request.getDescription(), errors);
            validateIngredients(request.getIngredients(), errors);
            validateInstructions(request.getInstructions(), errors);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateTitle(String title, Map<String, String> errors) {
        if (!StringUtils.hasText(title)) {
            errors.put("title", "Title must not be empty");
        }
    }

    private void validateDescription(String description, Map<String, String> errors) {
        if (!StringUtils.hasText(description)) {
            errors.put("description", "Description must not be empty");
        }
    }

    private void validateIngredients(List<String> ingredients, Map<String, String> errors) {
        if (ingredients == null || ingredients.isEmpty()) {
            errors.put("ingredients", "Ingredients must not be empty");
        } else if (ingredients.stream().anyMatch(ingredient -> !StringUtils.hasText(ingredient))) {
            errors.put("ingredients", "Each ingredient must be non-empty");
        }
    }

    private void validateInstructions(String instructions, Map<String, String> errors) {
        if (!StringUtils.hasText(instructions)) {
            errors.put("instructions", "Instructions must not be empty");
        }
    }
}
