package org.epde.ingrecipe.recipe.service;

import org.springframework.security.core.Authentication;
import org.epde.ingrecipe.recipe.dto.request.CommentRequest;
import org.epde.ingrecipe.recipe.dto.response.CommentResponse;

public interface CommentService {

    CommentResponse addComment(String authHeader, Authentication authentication, CommentRequest request);

    void moderateComment(Long commentId);

}
