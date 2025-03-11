package org.epde.ingrecipe.auth.validator;

import org.epde.ingrecipe.auth.dto.request.LoginRequest;
import org.epde.ingrecipe.common.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoginRequestValidator {

    public void validate(LoginRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request == null) {
            errors.put("request", "Request must not be null");
        } else {
            validateEmail(request.getEmail(), errors);
            validatePassword(request.getPassword(), errors);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateEmail(String email, Map<String, String> errors) {
        if (!StringUtils.hasText(email)) {
            errors.put("email", "Email must not be empty");
        }
    }

    private void validatePassword(String password, Map<String, String> errors) {
        if (!StringUtils.hasText(password)) {
            errors.put("password", "Password must not be empty");
        }
    }

}
