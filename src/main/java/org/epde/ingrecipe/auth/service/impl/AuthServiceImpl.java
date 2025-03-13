package org.epde.ingrecipe.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.auth.dto.request.LoginRequest;
import org.epde.ingrecipe.auth.dto.response.JwtTokenResponse;
import org.epde.ingrecipe.auth.token.repository.RevokedTokenRepository;
import org.epde.ingrecipe.auth.service.AuthService;
import org.epde.ingrecipe.auth.token.service.TokenService;
import org.epde.ingrecipe.auth.validator.LoginRequestValidator;
import org.epde.ingrecipe.common.util.UtilityHelper;
import org.epde.ingrecipe.user.model.Users;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final RevokedTokenRepository repository;
    private final TokenService tokenService;
    private final LoginRequestValidator validator;

    @Override
    public JwtTokenResponse login(LoginRequest request) {
        validator.validate(request);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        if (authentication.isAuthenticated()) {
            Users user = (Users) authentication.getPrincipal();
            return tokenService.generateToken(request.getEmail(), user);
        } else {
            throw new BadCredentialsException("Authentication failed for " + request.getEmail());
        }
    }

    @Override
    @Transactional
    public String logout(String authHeader, Authentication authentication) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Users user = (Users) authentication.getPrincipal();
            repository.invalidateToken(token, user, UtilityHelper.now());
            return "User logged out successfully.";
        }
        return "Invalid authorization header.";
    }
}
