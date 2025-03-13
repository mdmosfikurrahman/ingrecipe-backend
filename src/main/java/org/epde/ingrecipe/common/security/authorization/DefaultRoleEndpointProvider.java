package org.epde.ingrecipe.common.security.authorization;

import org.epde.ingrecipe.auth.role.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DefaultRoleEndpointProvider implements RoleEndpointProvider {

    @Override
    public List<String> getPublicEndpoints() {
        return List.of(
                "/auth/login/**",
                "/users/register/**",
                "/users/batch-register/**",
                "/recipes",
                "/recipes/search"
        );
    }

    @Override
    public List<String> getCommonSecureEndpoints() {
        return List.of(
                "/users/update-password",
                "/users/self-profile",
                "/recipes/unapproved"
        );
    }

    @Override
    public Map<String, List<String>> getRoleEndpoints() {
        return Map.of(
                Role.USER.name(), List.of(
                        "/recipes/add",
                        "/recipes/rate/**",
                        "/recipes/comment"
                ),
                Role.MODERATOR.name(), List.of(
                        "/recipes/moderate/**"
                ),
                Role.ADMIN.name(), List.of(
                        "/recipes/manage/**",
                        "/users/manage/**"
                )
        );
    }
}
