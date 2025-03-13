package org.epde.ingrecipe.common.security;

import lombok.RequiredArgsConstructor;
import org.epde.ingrecipe.auth.filter.JwtFilter;
import org.epde.ingrecipe.auth.role.Role;
import org.epde.ingrecipe.common.response.RestResponse;
import org.epde.ingrecipe.common.security.authorization.RoleEndpointProvider;
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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final JwtFilter jwtFilter;
    private final RoleEndpointProvider roleEndpointProvider;
    private static final int BCRYPT_STRENGTH = 12;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<String> publicEndpoints = roleEndpointProvider.getPublicEndpoints();
        List<String> commonSecureEndpoints = roleEndpointProvider.getCommonSecureEndpoints();
        Map<String, List<String>> roleEndpoints = roleEndpointProvider.getRoleEndpoints();

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(publicEndpoints.toArray(String[]::new)).permitAll();
                    requests.requestMatchers(commonSecureEndpoints.toArray(String[]::new))
                            .hasAnyRole(Role.USER.name(), Role.MODERATOR.name(), Role.ADMIN.name());

                    roleEndpoints.forEach((role, endpoints) ->
                            requests.requestMatchers(endpoints.toArray(String[]::new)).hasRole(role)
                    );

                    requests.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(unauthorizedHandler())
                                .accessDeniedHandler(forbiddenHandler())
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(BCRYPT_STRENGTH));
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedHandler() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writeResponse(response, RestResponse.error(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Invalid or missing token."));
        };
    }

    @Bean
    public AccessDeniedHandler forbiddenHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            writeResponse(response, RestResponse.error(HttpServletResponse.SC_FORBIDDEN, "Forbidden", "You do not have permission to access this resource."));
        };
    }

    private void writeResponse(HttpServletResponse response, RestResponse<?> restResponse) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = restResponse.toJson();
        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonResponse);
            writer.flush();
        }
    }
}
