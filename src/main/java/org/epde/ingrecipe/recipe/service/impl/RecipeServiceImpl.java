package org.epde.ingrecipe.recipe.service.impl;

import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.common.exception.UnauthorizedException;
import org.epde.ingrecipe.recipe.dto.request.RecipeRequest;
import org.epde.ingrecipe.recipe.dto.response.RecipeResponse;
import org.epde.ingrecipe.recipe.enums.RecipeStatus;
import org.epde.ingrecipe.recipe.model.Comment;
import org.epde.ingrecipe.recipe.model.Rating;
import org.epde.ingrecipe.recipe.model.Recipe;
import org.epde.ingrecipe.recipe.model.RecipeIngredient;
import org.epde.ingrecipe.recipe.repository.CommentRepository;
import org.epde.ingrecipe.recipe.repository.RatingRepository;
import org.epde.ingrecipe.recipe.repository.RecipeRepository;
import org.epde.ingrecipe.recipe.service.RecipeService;
import org.epde.ingrecipe.user.model.Users;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;

    private List<String> getCommentsByRecipeId(Long recipeId) {
        List<Comment> comments = commentRepository.findByRecipeId(recipeId);
        return comments.isEmpty() ? null : comments.stream().map(Comment::getContent).toList();
    }

    @Override
    public List<RecipeResponse> viewApprovedRecipes() {
        return recipeRepository.findByStatusWithIngredients(RecipeStatus.APPROVED)
                .stream()
                .map(recipe -> new RecipeResponse(recipe, getCommentsByRecipeId(recipe.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponse> viewUnapprovedRecipes() {
        return recipeRepository.findByStatusWithIngredients(RecipeStatus.PENDING)
                .stream()
                .map(recipe -> new RecipeResponse(recipe, getCommentsByRecipeId(recipe.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public RecipeResponse addRecipe(RecipeRequest request) {
        Recipe recipe = Recipe.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .instructions(request.getInstructions())
                .status(RecipeStatus.PENDING)
                .build();

        Recipe savedRecipe = recipeRepository.save(recipe);

        List<RecipeIngredient> ingredients = request.getIngredients().stream()
                .map(ingredient -> RecipeIngredient.builder()
                        .recipe(savedRecipe)
                        .ingredient(ingredient)
                        .build())
                .collect(Collectors.toList());

        savedRecipe.setIngredients(ingredients);
        recipeRepository.save(savedRecipe);

        return new RecipeResponse(savedRecipe, getCommentsByRecipeId(savedRecipe.getId()));
    }

    @Override
    public List<RecipeResponse> searchRecipes(String query) {
        return recipeRepository.findByTitleContainingIgnoreCaseAndStatus(query, RecipeStatus.APPROVED)
                .stream()
                .map(recipe -> new RecipeResponse(recipe, getCommentsByRecipeId(recipe.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RecipeResponse editRecipe(Long recipeId, RecipeRequest request) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        recipe.setTitle(request.getTitle());
        recipe.setDescription(request.getDescription());
        recipe.setInstructions(request.getInstructions());

        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            recipe.getIngredients().clear();
            List<RecipeIngredient> newIngredients = request.getIngredients().stream()
                    .map(ingredient -> RecipeIngredient.builder()
                            .recipe(recipe)
                            .ingredient(ingredient)
                            .build())
                    .toList();
            recipe.getIngredients().addAll(newIngredients);
        }

        recipeRepository.save(recipe);

        return new RecipeResponse(recipe, getCommentsByRecipeId(recipe.getId()));
    }

    @Override
    public void deleteRecipe(Long recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    @Override
    public void moderateRecipe(Long recipeId, Long actionId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        RecipeStatus status = RecipeStatus.fromValue(actionId);

        if (status == RecipeStatus.APPROVED) {
            recipe.setStatus(RecipeStatus.APPROVED);
            recipeRepository.save(recipe);
        } else if (status == RecipeStatus.REJECTED) {
            recipeRepository.delete(recipe);
        }
    }

    @Override
    @Transactional
    public void rateRecipe(Long recipeId, double rating, String authHeader, Authentication authentication) {
        if (!isValidAuth(authHeader, authentication)) {
            throw new UnauthorizedException("Invalid authentication!");
        }

        Users user = (Users) authentication.getPrincipal();

        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Rating ratingEntry = ratingRepository.findByRecipeIdAndUserId(recipeId, user.getId())
                .map(existingRating -> existingRating.toBuilder().rating(rating).build())
                .orElseGet(() -> Rating.builder()
                        .recipeId(recipeId)
                        .userId(user.getId())
                        .rating(rating)
                        .build());

        ratingRepository.save(ratingEntry);

        recipe.setRatings(ratingRepository.calculateAverageRating(recipeId));
        recipeRepository.save(recipe);
    }

    private boolean isValidAuth(String authHeader, Authentication authentication) {
        return authHeader != null && authHeader.startsWith("Bearer ") &&
                authentication != null && authentication.getPrincipal() instanceof Users;
    }

}
