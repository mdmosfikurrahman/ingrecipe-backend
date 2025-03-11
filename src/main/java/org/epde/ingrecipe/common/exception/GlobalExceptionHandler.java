package org.epde.ingrecipe.common.exception;

import org.epde.ingrecipe.common.response.ErrorDetails;
import org.epde.ingrecipe.common.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<RestResponse<List<ErrorDetails>>> buildErrorResponse(HttpStatus status, String message, List<ErrorDetails> errorDetails) {
        RestResponse<List<ErrorDetails>> response = RestResponse.error(status.value(), message, errorDetails);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<List<ErrorDetails>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDetails> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDetails(error.getField(), error.getDefaultMessage()))
                .toList();
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", errors);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<RestResponse<List<ErrorDetails>>> handleCustomValidationExceptions(ValidationException ex) {
        List<ErrorDetails> errors = ex.getErrors().entrySet().stream()
                .map(entry -> new ErrorDetails(entry.getKey(), entry.getValue()))
                .toList();
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Custom Validation Error", errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<RestResponse<List<ErrorDetails>>> handleNotFoundException(NotFoundException ex) {
        ErrorDetails errorResponse = new ErrorDetails("Not Found", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", List.of(errorResponse));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<RestResponse<List<ErrorDetails>>> handleBadRequestException(BadRequestException ex) {
        ErrorDetails errorResponse = new ErrorDetails("Bad Request", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", List.of(errorResponse));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestResponse<List<ErrorDetails>>> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorDetails errorResponse = new ErrorDetails("Unauthorized", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", List.of(errorResponse));
    }

    @ExceptionHandler(HashingException.class)
    public ResponseEntity<RestResponse<List<ErrorDetails>>> handleHashingException(HashingException ex) {
        ErrorDetails errorResponse = new ErrorDetails("Internal Server Error", ex.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", List.of(errorResponse));
    }

}
