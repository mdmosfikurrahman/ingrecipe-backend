package org.epde.ingrecipe.user.controller;

import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.auth.dto.response.JwtTokenResponse;
import org.epde.ingrecipe.common.response.RestResponse;
import org.epde.ingrecipe.user.dto.request.PasswordUpdateRequest;
import org.epde.ingrecipe.user.dto.request.UserRequest;
import org.epde.ingrecipe.user.dto.response.UserResponse;
import org.epde.ingrecipe.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/self-profile")
    public RestResponse<UserResponse> getSelfProfile(@RequestHeader("Authorization") String authHeader, Authentication authentication) {
        UserResponse response = service.getSelfProfile(authHeader, authentication);
        return RestResponse.success(HttpStatus.OK.value(), "Request Successful", response);
    }

    @GetMapping("/manage/get-user-profile/{id}")
    public RestResponse<UserResponse> getUserProfile(@PathVariable Long id) {
        UserResponse response = service.getUserProfile(id);
        return RestResponse.success(HttpStatus.OK.value(), "Request Successful", response);
    }

    @PostMapping("/register")
    public RestResponse<UserResponse> registerUser(@RequestBody UserRequest request) {
        UserResponse response = service.registerUser(request);
        return RestResponse.success(HttpStatus.CREATED.value(), "User registered successfully", response);
    }

    @PostMapping("/batch-register")
    public RestResponse<List<UserResponse>> registerUsers(@RequestBody List<UserRequest> requests) {
        List<UserResponse> responses = service.registerUsers(requests);
        return RestResponse.success(HttpStatus.CREATED.value(), "Users registered successfully", responses);
    }

    @PutMapping("/manage/{id}")
    public RestResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        UserResponse response = service.updateUser(id, request);
        return RestResponse.success(HttpStatus.OK.value(), "User updated successfully", response);
    }

    @PostMapping("/update-password")
    public RestResponse<JwtTokenResponse> updatePassword(@RequestHeader("Authorization") String authHeader, Authentication authentication, @RequestBody PasswordUpdateRequest request) {
        JwtTokenResponse response = service.updatePassword(authHeader, authentication, request);
        return RestResponse.success(HttpStatus.OK.value(), "Password updated successfully", response);
    }

    @DeleteMapping("/manage/{id}")
    public RestResponse<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return RestResponse.success(HttpStatus.OK.value(), "User deleted successfully", null);
    }

}
