package com.inn.SweetShop.JWT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Spring Security configuration class
 * Configures authentication, authorization, and JWT filter
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomerUsersDetailsService customerUsersDetailsService;
    private final JwtFilter jwtFilter;

    // Constructor injection for dependencies
    public SecurityConfig(CustomerUsersDetailsService customerUsersDetailsService, JwtFilter jwtFilter) {
        this.customerUsersDetailsService = customerUsersDetailsService;
        this.jwtFilter = jwtFilter;
    }

    // Bean for authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Password encoder using BCrypt for secure password hashing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Main security configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless JWT authentication
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no authentication required
                        .requestMatchers("/api/auth/login", "/api/auth/signup").permitAll()
                        .requestMatchers("/api/sweets", "/api/sweets/search").permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                // Use stateless session management since we're using JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT filter before Spring Security's authentication filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}