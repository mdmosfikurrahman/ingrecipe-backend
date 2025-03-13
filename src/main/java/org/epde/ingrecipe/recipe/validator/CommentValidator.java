package org.epde.ingrecipe.recipe.validator;

import org.epde.ingrecipe.common.exception.ValidationException;
import org.epde.ingrecipe.recipe.dto.request.CommentRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommentValidator {

    public void validate(CommentRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request == null) {
            errors.put("request", "Comment request must not be null");
        } else {
            validateRecipeId(request.getRecipeId(), errors);
            validateContent(request.getContent(), errors);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateRecipeId(Long recipeId, Map<String, String> errors) {
        if (recipeId == null) {
            errors.put("recipeId", "Recipe ID must not be null");
        }
    }

    private void validateContent(String content, Map<String, String> errors) {
        if (!StringUtils.hasText(content)) {
            errors.put("content", "Comment content must not be empty");
        }
    }
}
