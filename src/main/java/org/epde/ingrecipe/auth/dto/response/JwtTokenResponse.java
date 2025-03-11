package org.epde.ingrecipe.auth.dto.response;

public record JwtTokenResponse(String token, String issuedAt, String expiration) {
}
