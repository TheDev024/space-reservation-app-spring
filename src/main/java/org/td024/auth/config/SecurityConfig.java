package org.td024.auth.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.td024.auth.filter.JwtFilter;
import org.td024.auth.service.UserService;

import static org.springframework.http.HttpMethod.*;

@Configuration
//@EnableWebSecurity
@SecurityScheme(
        name = "bearer",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "JWT"
)
public class SecurityConfig {

    private final static String ADMIN = "ADMIN";
    private final static String USER = "USER";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, JwtFilter jwtFilter) throws Exception {
        return httpSecurity
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> customizer
                        // Authentication
                        .requestMatchers(POST, "/login", "/register").permitAll()
                        .requestMatchers(GET, "/refresh-token").permitAll()
                        // Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/javainuse-openapi/**").permitAll()
                        // Reservations
                        .requestMatchers(GET, "/reservations").hasRole(ADMIN)
                        .requestMatchers(GET, "/reservations/my").authenticated()
                        .requestMatchers("/workspaces/*/reservations/**").authenticated()
                        // Workspaces
                        .requestMatchers(GET, "/workspaces/*", "/workspaces").hasAnyRole(ADMIN, USER)
                        .requestMatchers(POST, "/workspaces").hasRole(ADMIN)
                        .requestMatchers(PUT, "/workspaces/*").hasRole(ADMIN)
                        .requestMatchers(DELETE, "/workspaces/*").hasRole(ADMIN)
                        // Block All Other Requests
                        .anyRequest().denyAll()
                ).build();
    }
}
