package com.inn.SweetShop.JWT;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT authentication filter that intercepts every request
 * Validates JWT tokens and sets up Spring Security context
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerUsersDetailsService service;

    private Claims claims = null;
    private String userName = null;

    /**
     * Main filter logic that runs once per request
     * Extracts and validates JWT token, then sets authentication in security context
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // Skip JWT validation for login and forgot password endpoints
        if (httpServletRequest.getServletPath().matches("/api/auth/login|/api/auth/forgotPassword")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            // Extract token from Authorization header
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String token = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                userName = jwtUtil.extractUsername(token);
                claims = jwtUtil.extractAllClaims(token);
            }

            // Validate token and set authentication if valid
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = service.loadUserByUsername(userName);

                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    // Check if current user has admin role
    public boolean isAdmin() {
        return claims != null && "admin".equalsIgnoreCase((String) claims.get("role"));
    }

    // Check if current user has user role
    public boolean isUser() {
        return claims != null && "user".equalsIgnoreCase((String) claims.get("role"));
    }

    // Get current authenticated username
    public String getCurrentUser() {
        return userName;
    }
}