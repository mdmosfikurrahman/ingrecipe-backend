package org.epde.ingrecipe.auth.service;

import javax.crypto.SecretKey;

public interface JwtService {
    String extractEmail(String token);
    SecretKey getKey();
}
