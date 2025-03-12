package org.epde.ingrecipe.auth.token.service;

import org.epde.ingrecipe.auth.dto.response.JwtTokenResponse;
import org.epde.ingrecipe.user.model.Users;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("unused")
public interface TokenService {
    boolean isTokenRevoked(String token);
    boolean isTokenExpired(String token);
    boolean validateToken(String token, UserDetails userDetails);
    void cleanUpExpiredTokens();
    String extractTokenExpiration(String token);
    JwtTokenResponse generateToken(String username, Users userId);
}
