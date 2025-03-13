package org.epde.ingrecipe.recipe.service;

import org.epde.ingrecipe.recipe.dto.request.CommentRequest;
import org.epde.ingrecipe.recipe.dto.response.CommentResponse;

public interface CommentService {

    CommentResponse addComment(CommentRequest request);

    void moderateComment(Long commentId);

}
