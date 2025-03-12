package org.epde.ingrecipe.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.auth.dto.response.JwtTokenResponse;
import org.epde.ingrecipe.auth.token.service.TokenService;
import org.epde.ingrecipe.common.exception.BadRequestException;
import org.epde.ingrecipe.common.exception.NotFoundException;
import org.epde.ingrecipe.user.dto.request.PasswordUpdateRequest;
import org.epde.ingrecipe.user.dto.request.UserRequest;
import org.epde.ingrecipe.user.dto.response.UserResponse;
import org.epde.ingrecipe.user.model.Users;
import org.epde.ingrecipe.user.repository.UserRepository;
import org.epde.ingrecipe.user.service.UserService;
import org.epde.ingrecipe.user.validator.UserRequestValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final TokenService tokenService;
    private final UserRepository repository;
    private final UserRequestValidator validator;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    private static final String USER_NOT_FOUND_MESSAGE = "User with ID %s not found.";

    @Override
    public UserResponse getUser(Long id) {
        return repository.findById(id)
                .map(this::createUserResponse)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserResponse registerUser(UserRequest request) {
        validator.validate(request);
        checkIfUserExists(request);

        Users newUser = buildUserForCreate(request);
        Users savedUser = repository.save(newUser);
        return createUserResponse(savedUser);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        validator.validate(request);
        Users existingUser = getUserById(id);

        Users updatedUser = buildUserForUpdate(existingUser, request);
        repository.save(updatedUser);
        return createUserResponse(updatedUser);
    }

    @Override
    public JwtTokenResponse updatePassword(Long id, PasswordUpdateRequest request) {
        Users existingUser = getUserById(id);
        existingUser.setPassword(encodePassword(request.getNewPassword()));
        repository.save(existingUser);
        return tokenService.generateToken(existingUser.getUsername(), existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        repository.findById(id)
                .ifPresentOrElse(
                        user -> repository.deleteById(id),
                        () -> { throw new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id)); }
                );
    }

    @Override
    public boolean userExists(Long id) {
        return repository.existsById(id);
    }

    private void checkIfUserExists(UserRequest request) {
        repository.findByUsernameOrEmail(request.getUsername(), request.getEmail()).ifPresent(user -> {
            if (user.getUsername().equals(request.getUsername())) {
                throw new BadRequestException("Username already exists.");
            }
            if (user.getEmail().equals(request.getEmail())) {
                throw new BadRequestException("Email already exists.");
            }
        });
    }

    private Users getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id)));
    }

    private UserResponse createUserResponse(Users user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    private Users buildUserForCreate(UserRequest request) {
        return Users.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodePassword(request.getPassword()))
                .role(request.getRole())
                .build();
    }

    private Users buildUserForUpdate(Users existingUser, UserRequest request) {
        return Users.builder()
                .id(existingUser.getId())
                .username(request.getUsername())
                .email(existingUser.getEmail())
                .password(encodePassword(request.getPassword()))
                .role(request.getRole())
                .build();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
