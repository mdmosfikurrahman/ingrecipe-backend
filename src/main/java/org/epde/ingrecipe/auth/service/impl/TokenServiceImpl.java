package org.epde.ingrecipe.auth.service.impl;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.auth.dto.response.JwtTokenResponse;
import org.epde.ingrecipe.auth.model.TokenBlackList;
import org.epde.ingrecipe.auth.repository.TokenBlackListRepository;
import org.epde.ingrecipe.auth.service.JwtService;
import org.epde.ingrecipe.auth.service.TokenService;
import org.epde.ingrecipe.user.model.Users;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final TokenBlackListRepository repository;

    @Override
    public boolean isTokenBlacklisted(String token) {
        return repository.existsByTokenAndInvalidatedAtIsNotNull(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        TokenBlackList tokenBlackList = repository.findByToken(token);
        if (tokenBlackList != null) {
            return tokenBlackList.getExpiresAt().isBefore(LocalDateTime.now());
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
        List<TokenBlackList> tokens = repository.findAll();

        tokens.stream()
                .filter(tokenBlackList -> isTokenExpired(tokenBlackList.getToken()))
                .forEach(token -> {
                    token.setInvalidatedAt(token.getExpiresAt());
                    repository.save(token);
                });
    }

    @Override
    public String extractTokenExpiration(String token) {
        TokenBlackList tokenBlackList = repository.findByToken(token);
        LocalDateTime expirationTime = tokenBlackList.getExpiresAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy hh:mm:ss a");
        return expirationTime.format(formatter);
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    @Transactional
    public JwtTokenResponse generateToken(String username, Users user) {
        TokenBlackList existingToken = repository.findByUserAndInvalidatedAtIsNull(user);

        if (existingToken != null) {
            repository.invalidateToken(existingToken.getToken(), user, LocalDateTime.now());
        }

        Map<String, Object> claims = new HashMap<>();
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        String token = Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .and()
                .signWith(jwtService.getKey())
                .compact();

        TokenBlackList tokenBlacklist = TokenBlackList.builder()
                .token(token)
                .user(user)
                .invalidatedAt(null)
                .expiresAt(convertToLocalDateTimeViaInstant(expiration))
                .build();

        repository.save(tokenBlacklist);

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy hh:mm:ss a");

        return new JwtTokenResponse(token, formatter.format(issuedAt), formatter.format(expiration));
    }

}