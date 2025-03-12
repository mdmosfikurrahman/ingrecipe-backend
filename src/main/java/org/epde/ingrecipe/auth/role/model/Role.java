package org.epde.ingrecipe.auth.role.model;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Collections;

@Getter
public enum Role {
    ADMIN(1),
    MODERATOR(2),
    USER(3);

    private final long value;

    private static final Map<Long, Role> ROLE_MAP = Collections.unmodifiableMap(
            Stream.of(Role.values()).collect(Collectors.toMap(Role::getValue, role -> role))
    );

    Role(long value) {
        this.value = value;
    }

    public static Role fromValue(long value) {
        Role role = ROLE_MAP.get(value);
        if (role == null) {
            throw new IllegalArgumentException("Invalid role ID: " + value + ". Expected values: " + ROLE_MAP.keySet());
        }
        return role;
    }
}
