package org.epde.ingrecipe.recipe.service.impl;

import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.recipe.dto.request.CommentRequest;
import org.epde.ingrecipe.recipe.dto.response.CommentResponse;
import org.epde.ingrecipe.recipe.model.Comment;
import org.epde.ingrecipe.recipe.repository.CommentRepository;
import org.epde.ingrecipe.recipe.service.CommentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public CommentResponse addComment(CommentRequest request) {
        Comment comment = Comment.builder()
                .recipeId(request.getRecipeId())
                .content(request.getContent())
                .build();
        Comment savedComment = commentRepository.save(comment);
        return new CommentResponse(savedComment);
    }

    @Override
    public void moderateComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
