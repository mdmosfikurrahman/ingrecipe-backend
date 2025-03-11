package org.epde.ingrecipe.user.validator;

import org.epde.ingrecipe.common.exception.ValidationException;
import org.epde.ingrecipe.user.dto.request.UserRequest;
import org.epde.ingrecipe.user.model.Role;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserRequestValidator {

    public void validate(UserRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request == null) {
            errors.put("request", "Request must not be null");
        } else {
            validateUsername(request.getUsername(), errors);
            validateEmail(request.getEmail(), errors);
            validatePassword(request.getPassword(), errors);
            validateRole(request.getRole(), errors);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateUsername(String username, Map<String, String> errors) {
        if (!StringUtils.hasText(username)) {
            errors.put("username", "Username must not be empty");
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

    private void validateRole(Role role, Map<String, String> errors) {
        if (role == null) {
            errors.put("role", "Role must not be null");
        }
    }

}
