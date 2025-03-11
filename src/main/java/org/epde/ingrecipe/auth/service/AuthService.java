package org.epde.ingrecipe.auth.service;

import org.epde.ingrecipe.auth.dto.request.LoginRequest;
import org.epde.ingrecipe.auth.dto.response.JwtTokenResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {
    JwtTokenResponse login(LoginRequest request);
    String logout(String authHeader, Authentication authentication);
}
