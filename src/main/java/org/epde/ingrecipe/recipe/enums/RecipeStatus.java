package org.epde.ingrecipe.recipe.enums;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Collections;

@Getter
public enum RecipeStatus {
    PENDING(1),
    APPROVED(2),
    REJECTED(3);

    private final long value;

    private static final Map<Long, RecipeStatus> STATUS_MAP = Collections.unmodifiableMap(
            Stream.of(RecipeStatus.values()).collect(Collectors.toMap(RecipeStatus::getValue, status -> status))
    );

    RecipeStatus(long value) {
        this.value = value;
    }

    public static RecipeStatus fromValue(long value) {
        RecipeStatus status = STATUS_MAP.get(value);
        if (status == null) {
            throw new IllegalArgumentException("Invalid recipe status ID: " + value + ". Expected values: " + STATUS_MAP.keySet());
        }
        return status;
    }
}
