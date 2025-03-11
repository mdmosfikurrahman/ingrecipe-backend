package org.epde.ingrecipe.common.config;

import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.auth.filter.JwtFilter;
import org.epde.ingrecipe.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final JwtFilter jwtFilter;

    private static final List<String> PUBLIC_API_ENDPOINTS = List.of(
            "/auth/login/**",
            "/non-secure/**",
            "/users/register/**"
//            - View recipes
//            - Search for recipes by ingredients
    );

    private static final List<String> USER_API_ENDPOINTS = List.of(
//            - All guest permissions
//            - Add personal recipes
//            - Save favorite recipes
//            - Comment on recipes

    );


    private static final List<String> MODERATOR_API_ENDPOINTS = List.of(
//            - Approve/reject user-submitted recipes
//            - Moderate comments
//            - Edit recipes for formatting/errors
    );

    private static final List<String> ADMIN_API_ENDPOINTS = List.of(
//            - All moderator permissions
//            - Manage users
//            - Delete/edit any recipe
//            - Manage system settings
    );

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var roleEndpointsMap = Map.ofEntries(
                Map.entry("PUBLIC", PUBLIC_API_ENDPOINTS),
                Map.entry("ADMIN", ADMIN_API_ENDPOINTS),
                Map.entry("MODERATOR", MODERATOR_API_ENDPOINTS),
                Map.entry("USER", USER_API_ENDPOINTS)
        );

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(PUBLIC_API_ENDPOINTS.toArray(String[]::new)).permitAll();
                    roleEndpointsMap.forEach((role, endpoints) ->
                            request.requestMatchers(endpoints.toArray(String[]::new)).hasRole(role)
                    );
                    request.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
