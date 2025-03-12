package org.epde.ingrecipe.auth.token.service.impl;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.auth.dto.response.JwtTokenResponse;
import org.epde.ingrecipe.auth.service.JwtService;
import org.epde.ingrecipe.auth.token.model.RevokedToken;
import org.epde.ingrecipe.auth.token.repository.RevokedTokenRepository;
import org.epde.ingrecipe.auth.token.service.TokenService;
import org.epde.ingrecipe.user.model.Users;
import org.epde.ingrecipe.util.DateTimeUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (revokedToken != null) {
            LocalDateTime nowBDT = DateTimeUtil.convertToBDT(LocalDateTime.now());
            return revokedToken.getExpiresAt().isBefore(nowBDT);
        }
        return true;
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
                .filter(revokedToken -> isTokenExpired(revokedToken.getToken()))
                .forEach(token -> {
                    token.setInvalidatedAt(token.getExpiresAt());
                    repository.save(token);
                });
    }

    @Override
    public String extractTokenExpiration(String token) {
        RevokedToken revokedToken = repository.findByToken(token);
        if (revokedToken != null) {
            return DateTimeUtil.formatToBDT(revokedToken.getExpiresAt());
        }
        return "N/A";
    }

    @Override
    @Transactional
    public JwtTokenResponse generateToken(String username, Users user) {
        RevokedToken existingToken = repository.findByUserAndInvalidatedAtIsNull(user);

        if (existingToken != null) {
            repository.invalidateToken(existingToken.getToken(), user, DateTimeUtil.nowBDT());
        }

        Map<String, Object> claims = new HashMap<>();

        LocalDateTime issuedAtBDT = DateTimeUtil.nowBDT();
        LocalDateTime expirationBDT = issuedAtBDT.plusHours(1);

        Date issuedAt = DateTimeUtil.convertToDate(issuedAtBDT);
        Date expiration = DateTimeUtil.convertToDate(expirationBDT);

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
                .expiresAt(expirationBDT)
                .build();

        repository.save(revokedToken);

        return new JwtTokenResponse(
                token,
                DateTimeUtil.formatToBDT(issuedAtBDT),
                DateTimeUtil.formatToBDT(expirationBDT)
        );
    }

}
