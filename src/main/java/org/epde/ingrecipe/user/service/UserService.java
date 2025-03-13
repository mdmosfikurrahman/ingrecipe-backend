package org.epde.ingrecipe.user.service;

import org.epde.ingrecipe.auth.dto.response.JwtTokenResponse;
import org.epde.ingrecipe.user.dto.request.PasswordUpdateRequest;
import org.epde.ingrecipe.user.dto.request.UserRequest;
import org.epde.ingrecipe.user.dto.response.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserResponse> getAllUsers();
    UserResponse getSelfProfile(String authHeader, Authentication authentication);
    UserResponse getUserProfile(Long id);
    UserResponse registerUser(UserRequest request);
    UserResponse updateUser(Long id, UserRequest request);
    JwtTokenResponse updatePassword(String authHeader, Authentication authentication, PasswordUpdateRequest request);
    void deleteUser(Long id);
}
