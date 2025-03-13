package org.epde.ingrecipe.auth.controller;

import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.auth.dto.request.LoginRequest;
import org.epde.ingrecipe.auth.dto.response.JwtTokenResponse;
import org.epde.ingrecipe.auth.service.AuthService;
import org.epde.ingrecipe.auth.validator.LoginRequestValidator;
import org.epde.ingrecipe.common.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final LoginRequestValidator validator;

    @PostMapping("/login")
    public RestResponse<JwtTokenResponse> login(@RequestBody LoginRequest request) {
        validator.validate(request);
        JwtTokenResponse jwtTokenResponse = authService.login(request);
        return RestResponse.success(HttpStatus.OK.value(), "Login successful", jwtTokenResponse);
    }

    @PostMapping("/logout")
    public RestResponse<String> logout(@RequestHeader("Authorization") String authHeader, Authentication authentication) {
        String response = authService.logout(authHeader, authentication);
        return RestResponse.success(HttpStatus.OK.value(), "Logout successful", response);
    }

}
