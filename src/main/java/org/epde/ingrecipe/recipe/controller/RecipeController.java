package org.epde.ingrecipe.recipe.controller;

import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.common.response.RestResponse;
import org.epde.ingrecipe.recipe.dto.request.CommentRequest;
import org.epde.ingrecipe.recipe.dto.request.RecipeRequest;
import org.epde.ingrecipe.recipe.dto.response.CommentResponse;
import org.epde.ingrecipe.recipe.dto.response.RecipeResponse;
import org.epde.ingrecipe.recipe.service.CommentService;
import org.epde.ingrecipe.recipe.service.RecipeService;
import org.epde.ingrecipe.recipe.validator.CommentValidator;
import org.epde.ingrecipe.recipe.validator.RecipeValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final CommentService commentService;
    private final RecipeValidator recipeValidator;
    private final CommentValidator commentValidator;

    /**
     * View all approved recipes
     */
    @GetMapping
    public RestResponse<List<RecipeResponse>> viewApprovedRecipes() {
        List<RecipeResponse> recipes = recipeService.viewApprovedRecipes();
        return RestResponse.success(HttpStatus.OK.value(), "Success", recipes);
    }

    /**
     * View all recipes
     */
    @GetMapping("/unapproved")
    public RestResponse<List<RecipeResponse>> viewUnapprovedRecipes() {
        List<RecipeResponse> recipes = recipeService.viewUnapprovedRecipes();
        return RestResponse.success(HttpStatus.OK.value(), "Success", recipes);
    }

    /**
     * Add a new recipe
     */
    @PostMapping("/add")
    public RestResponse<RecipeResponse> addRecipe(@RequestBody RecipeRequest request) {
        recipeValidator.validate(request);
        RecipeResponse response = recipeService.addRecipe(request);
        return RestResponse.success(HttpStatus.CREATED.value(), "Recipe added successfully", response);
    }

    /**
     * Search for recipes
     */
    @GetMapping("/search")
    public RestResponse<List<RecipeResponse>> searchRecipes(@RequestParam String query) {
        List<RecipeResponse> results = recipeService.searchRecipes(query);
        return RestResponse.success(HttpStatus.OK.value(), "Search results", results);
    }

    /**
     * Add a comment to a recipe
     */
    @PostMapping("/comment")
    public RestResponse<CommentResponse> addComment(@RequestBody CommentRequest request) {
        commentValidator.validate(request);
        CommentResponse response = commentService.addComment(request);
        return RestResponse.success(HttpStatus.OK.value(), "Comment added successfully", response);
    }

    /**
     * Moderate a recipe (approve/reject)
     */
    @PostMapping("/moderate/{recipeId}/{actionId}")
    public RestResponse<Void> moderateRecipe(@PathVariable Long recipeId, @PathVariable Long actionId) {
        try {
            recipeService.moderateRecipe(recipeId, actionId);
            return RestResponse.success(HttpStatus.OK.value(), "Recipe moderation action executed successfully", null);
        } catch (IllegalArgumentException e) {
            return RestResponse.error(HttpStatus.BAD_REQUEST.value(), "Invalid moderation action", null);
        }
    }

    /**
     * Edit an existing recipe
     */
    @PutMapping("/moderate/edit/{recipeId}")
    public RestResponse<RecipeResponse> editRecipe(@PathVariable Long recipeId, @RequestBody RecipeRequest request) {
        recipeValidator.validate(request);
        RecipeResponse response = recipeService.editRecipe(recipeId, request);
        return RestResponse.success(HttpStatus.OK.value(), "Recipe edited successfully", response);
    }

    /**
     * Delete a comment
     */
    @DeleteMapping("/moderate/comment/{commentId}")
    public RestResponse<Void> moderateComment(@PathVariable Long commentId) {
        commentService.moderateComment(commentId);
        return RestResponse.success(HttpStatus.OK.value(), "Comment deleted successfully", null);
    }

    /**
     * Admin deletes a recipe
     */
    @DeleteMapping("/manage/{recipeId}")
    public RestResponse<Void> adminDeleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return RestResponse.success(HttpStatus.OK.value(), "Recipe deleted successfully", null);
    }

    /**
     * Rate a recipe
     */
    @PostMapping("/rate/{recipeId}")
    public RestResponse<Void> rateRecipe(@PathVariable Long recipeId, @RequestParam double rating, @RequestHeader("Authorization") String authHeader, Authentication authentication) {
        recipeService.rateRecipe(recipeId, rating, authHeader, authentication);
        return RestResponse.success(HttpStatus.OK.value(), "Recipe rated successfully", null);
    }
}
