package org.epde.ingrecipe.recipe.dto.request;

import lombok.Data;

@Data
public class CommentRequest {
    private Long recipeId;
    private String content;
}
