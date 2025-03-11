package org.epde.ingrecipe.user.service;

import org.epde.ingrecipe.auth.dto.response.JwtTokenResponse;
import org.epde.ingrecipe.user.dto.request.PasswordUpdateRequest;
import org.epde.ingrecipe.user.dto.request.UserRequest;
import org.epde.ingrecipe.user.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserResponse getUser(Long id);
    UserResponse registerUser(UserRequest request);
    UserResponse updateUser(Long id, UserRequest request);
    JwtTokenResponse updatePassword(Long id, PasswordUpdateRequest request);
    void deleteUser(Long id);
    boolean userExists(Long id);
}
