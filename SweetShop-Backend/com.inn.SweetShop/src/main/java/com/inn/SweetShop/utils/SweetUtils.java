package com.inn.SweetShop.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for common operations in SweetShop application
 */
@Slf4j
public class SweetUtils {

    // Private constructor to prevent instantiation
    private SweetUtils() {
    }

    // Helper method to create standardized JSON response
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", httpStatus);
    }
}