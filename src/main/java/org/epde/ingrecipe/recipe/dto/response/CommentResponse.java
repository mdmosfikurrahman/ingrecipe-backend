package org.epde.ingrecipe.recipe.dto.response;

import lombok.Data;
import org.epde.ingrecipe.recipe.model.Comment;

@Data
public class CommentResponse {
    private Long id;
    private Long recipeId;
    private String content;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.recipeId = comment.getRecipeId();
        this.content = comment.getContent();
    }
}
