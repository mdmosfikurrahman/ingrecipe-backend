package org.epde.ingrecipe.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    private final String message;

    public UnauthorizedException(String message) {
        super(message);
        this.message = message;
    }
}
