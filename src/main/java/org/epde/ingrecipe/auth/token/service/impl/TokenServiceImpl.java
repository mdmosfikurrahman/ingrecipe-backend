package org.epde.ingrecipe.auth.token.service.impl;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.auth.dto.response.JwtTokenResponse;
import org.epde.ingrecipe.auth.token.model.RevokedToken;
import org.epde.ingrecipe.auth.token.repository.RevokedTokenRepository;
import org.epde.ingrecipe.auth.service.JwtService;
import org.epde.ingrecipe.auth.token.service.TokenService;
import org.epde.ingrecipe.user.model.Users;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        RevokedToken RevokedToken = repository.findByToken(token);
        if (RevokedToken != null) {
            return RevokedToken.getExpiresAt().isBefore(LocalDateTime.now(ZoneId.of("Asia/Dhaka")));
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
                .filter(RevokedToken -> isTokenExpired(RevokedToken.getToken()))
                .forEach(token -> {
                    token.setInvalidatedAt(token.getExpiresAt());
                    repository.save(token);
                });
    }

    @Override
    public String extractTokenExpiration(String token) {
        RevokedToken RevokedToken = repository.findByToken(token);
        LocalDateTime expirationTime = RevokedToken.getExpiresAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy hh:mm:ss a");
        return expirationTime.format(formatter);
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.of("Asia/Dhaka")).toLocalDateTime();
    }

    @Override
    @Transactional
    public JwtTokenResponse generateToken(String username, Users user) {
        RevokedToken existingToken = repository.findByUserAndInvalidatedAtIsNull(user);

        if (existingToken != null) {
            repository.invalidateToken(existingToken.getToken(), user, LocalDateTime.now(ZoneId.of("Asia/Dhaka")));
        }

        Map<String, Object> claims = new HashMap<>();

        ZonedDateTime issuedAtZdt = Instant.now().atZone(ZoneId.of("Asia/Dhaka"));
        ZonedDateTime expirationZdt = issuedAtZdt.plusHours(1);

        Date issuedAt = Date.from(issuedAtZdt.toInstant());
        Date expiration = Date.from(expirationZdt.toInstant());

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
                .expiresAt(convertToLocalDateTimeViaInstant(expiration))
                .build();

        repository.save(revokedToken);

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy hh:mm:ss a");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));

        return new JwtTokenResponse(token, formatter.format(issuedAt), formatter.format(expiration));
    }

}