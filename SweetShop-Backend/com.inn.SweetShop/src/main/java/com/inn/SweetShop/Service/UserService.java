package com.inn.SweetShop.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Service interface for user-related operations
 */
public interface UserService {

    // Register new user
    ResponseEntity<String> signUp(Map<String, String> requestMap);

    // Authenticate user and generate token
    ResponseEntity<String> login(Map<String, String> requestMap);

    // Validate JWT token
    ResponseEntity<String> checkToken();

    // Update user information
    ResponseEntity<String> update(Map<String, String> requestMap);
}