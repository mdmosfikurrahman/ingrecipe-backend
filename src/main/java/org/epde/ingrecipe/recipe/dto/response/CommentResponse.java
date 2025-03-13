package org.epde.ingrecipe.recipe.dto.response;

import lombok.Data;
import org.epde.ingrecipe.recipe.model.Comment;
import org.epde.ingrecipe.user.model.Users;

@Data
public class CommentResponse {
    private String name;
    private String content;

    public CommentResponse(Comment comment, Users user) {
        this.name = (user != null) ? user.getFirstName() + " " + user.getLastName() : "Unknown";
        this.content = comment.getContent();
    }
}
