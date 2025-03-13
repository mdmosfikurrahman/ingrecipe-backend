package org.epde.ingrecipe.recipe.controller;

import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.common.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes")
public class RecipeController {

    @PostMapping("/add")
    public RestResponse<String> addRecipe() {
        return RestResponse.success(HttpStatus.CREATED.value(), "Created", "Recipe added");
    }

    @GetMapping
    public RestResponse<String> viewRecipes() {
        return RestResponse.success(HttpStatus.OK.value(), "Success", "Viewing all recipes");
    }

    @GetMapping("/search")
    public RestResponse<String> searchRecipes() {
        return RestResponse.success(HttpStatus.OK.value(), "Success", "Search results for recipes");
    }

    @PostMapping("/favorite")
    public RestResponse<String> saveFavoriteRecipe() {
        return RestResponse.success(HttpStatus.OK.value(), "Success", "Recipe marked as favorite");
    }

    @PostMapping("/comment")
    public RestResponse<String> addComment() {
        return RestResponse.success(HttpStatus.OK.value(), "Success", "Comment added to recipe");
    }

    @PostMapping("/moderate/{action}")
    public RestResponse<String> moderateRecipe(@PathVariable String action) {
        return RestResponse.success(HttpStatus.OK.value(), "Success", "Recipe moderation action: " + action);
    }

    @PutMapping("/moderate/edit/{recipeId}")
    public RestResponse<String> editRecipe(@PathVariable String recipeId) {
        return RestResponse.success(HttpStatus.OK.value(), "Success", "Recipe with ID " + recipeId + " edited for formatting/errors");
    }

    @DeleteMapping("/manage/{recipeId}")
    public RestResponse<String> adminDeleteRecipe(@PathVariable String recipeId) {
        return RestResponse.success(HttpStatus.OK.value(), "Success", "Recipe with ID " + recipeId + " deleted");
    }

    @DeleteMapping("/moderate/comment/{commentId}")
    public RestResponse<String> moderateComment(@PathVariable String commentId) {
        return RestResponse.success(HttpStatus.OK.value(), "Success", "Comment with ID " + commentId + " has been moderated");
    }
}
