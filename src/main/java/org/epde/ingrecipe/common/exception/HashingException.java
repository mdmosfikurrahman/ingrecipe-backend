package org.epde.ingrecipe.common.exception;

import lombok.Getter;

@Getter
public class HashingException extends RuntimeException {
    private final String message;

    public HashingException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
