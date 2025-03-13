package org.epde.ingrecipe.recipe.service.impl;

import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.common.exception.UnauthorizedException;
import org.epde.ingrecipe.recipe.dto.request.CommentRequest;
import org.epde.ingrecipe.recipe.dto.response.CommentResponse;
import org.epde.ingrecipe.recipe.model.Comment;
import org.epde.ingrecipe.recipe.repository.CommentRepository;
import org.epde.ingrecipe.recipe.service.CommentService;
import org.epde.ingrecipe.user.model.Users;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public CommentResponse addComment(String authHeader, Authentication authentication, CommentRequest request) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            if (authentication != null && authentication.getPrincipal() instanceof Users user) {
                Comment comment = Comment.builder()
                        .recipeId(request.getRecipeId())
                        .userId(user.getId())
                        .content(request.getContent())
                        .build();
                Comment savedComment = commentRepository.save(comment);
                return new CommentResponse(savedComment, user);
            }
        }
        throw new UnauthorizedException("Invalid authentication!");

    }

    @Override
    public void moderateComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
