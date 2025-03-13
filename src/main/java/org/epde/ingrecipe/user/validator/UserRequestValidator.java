package org.epde.ingrecipe.user.validator;

import org.epde.ingrecipe.common.exception.ValidationException;
import org.epde.ingrecipe.user.dto.request.UserRequest;
import org.epde.ingrecipe.auth.role.Role;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class UserRequestValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );

    public void validate(UserRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request == null) {
            errors.put("request", "Request must not be null");
        } else {
            validateFirstName(request.getFirstName(), errors);
            validateLastName(request.getLastName(), errors);
            validateUsername(request.getUsername(), errors);
            validateEmail(request.getEmail(), errors);
            validatePassword(request.getPassword(), errors);
            validateRole(request.getRole(), errors);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateFirstName(String firstName, Map<String, String> errors) {
        if (!StringUtils.hasText(firstName)) {
            errors.put("firstName", "First name must not be empty");
        }
    }

    private void validateLastName(String lastName, Map<String, String> errors) {
        if (!StringUtils.hasText(lastName)) {
            errors.put("lastName", "Last name must not be empty");
        }
    }

    private void validateUsername(String username, Map<String, String> errors) {
        if (!StringUtils.hasText(username)) {
            errors.put("username", "Username must not be empty");
        } else if (username.length() < 4 || username.length() > 20) {
            errors.put("username", "Username must be between 4 and 20 characters");
        }
    }

    private void validateEmail(String email, Map<String, String> errors) {
        if (!StringUtils.hasText(email)) {
            errors.put("email", "Email must not be empty");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.put("email", "Invalid email format");
        }
    }

    private void validatePassword(String password, Map<String, String> errors) {
        if (!StringUtils.hasText(password)) {
            errors.put("password", "Password must not be empty");
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errors.put("password", "Password must be at least 8 characters long, contain at least one digit, one uppercase letter, one lowercase letter, and one special character");
        }
    }

    private void validateRole(Role role, Map<String, String> errors) {
        if (role == null) {
            errors.put("role", "Role must not be null");
        }
    }
}
