package org.epde.ingrecipe.auth.token.service.impl;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.auth.dto.response.JwtTokenResponse;
import org.epde.ingrecipe.auth.token.model.RevokedToken;
import org.epde.ingrecipe.auth.token.repository.RevokedTokenRepository;
import org.epde.ingrecipe.auth.service.JwtService;
import org.epde.ingrecipe.auth.token.service.TokenService;
import org.epde.ingrecipe.common.util.DateTimeUtil;
import org.epde.ingrecipe.user.model.Users;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final RevokedTokenRepository repository;

    @Override
    public boolean isTokenRevoked(String token) {
        return repository.existsByTokenAndInvalidatedAtIsNotNull(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        RevokedToken revokedToken = repository.findByToken(token);
        return revokedToken == null || revokedToken.getExpiresAt().isBefore(DateTimeUtil.now());
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = jwtService.extractEmail(token);
        return (email.equals(((Users) userDetails).getEmail())) && !isTokenExpired(token);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    @Override
    public void cleanUpExpiredTokens() {
        List<RevokedToken> tokens = repository.findAll();
        tokens.stream()
                .filter(token -> isTokenExpired(token.getToken()))
                .forEach(token -> {
                    token.setInvalidatedAt(token.getExpiresAt());
                    repository.save(token);
                });
    }

    @Override
    public String extractTokenExpiration(String token) {
        RevokedToken revokedToken = repository.findByToken(token);
        return revokedToken != null ? DateTimeUtil.format(revokedToken.getExpiresAt()) : "Expired/Invalid Token";
    }

    @Override
    @Transactional
    public JwtTokenResponse generateToken(String username, Users user) {
        RevokedToken existingToken = repository.findByUserAndInvalidatedAtIsNull(user);

        if (existingToken != null) {
            repository.invalidateToken(existingToken.getToken(), user, DateTimeUtil.now());
        }

        Map<String, Object> claims = new HashMap<>();
        Date issuedAt = DateTimeUtil.toDate(DateTimeUtil.now());
        Date expiration = DateTimeUtil.toDate(DateTimeUtil.now().plusHours(1));

        String token = Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .and()
                .signWith(jwtService.getKey())
                .compact();

        RevokedToken revokedToken = RevokedToken.builder()
                .token(token)
                .user(user)
                .invalidatedAt(null)
                .expiresAt(DateTimeUtil.toLocalDateTime(expiration))
                .build();

        repository.save(revokedToken);

        return new JwtTokenResponse(token, DateTimeUtil.format(issuedAt), DateTimeUtil.format(expiration));
    }
}
