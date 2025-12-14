package com.inn.SweetShop.Rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;

/**
 * REST controller interface for user authentication
 */
@RequestMapping(path = "/api/auth")
public interface UserRest {

    // Register new user
    @PostMapping(path = "/signup")
    ResponseEntity<String> signUp(@RequestBody Map<String, String> requestMap);

    // User login
    @PostMapping(path = "/login")
    ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);

    // Update user details
    @PostMapping(path = "/update")
    ResponseEntity<String> update(@RequestBody(required = true) Map<String, String> RequestMap);

    // Check if token is valid
    @GetMapping(path = "/checkToken")
    ResponseEntity<String> checkToken();
}