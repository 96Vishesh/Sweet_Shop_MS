package com.inn.SweetShop.Rest;

import com.inn.SweetShop.POJO.Sweet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/sweets")
public interface SweetRest {

    // Add a new sweet to inventory
    @PostMapping
    ResponseEntity<String> addSweet(@RequestBody Map<String, String> requestMap);

    // Get all sweets
    @GetMapping
    ResponseEntity<List<Sweet>> getAllSweets();

    // Search sweets by name, category, or price range
    @GetMapping("/search")
    ResponseEntity<List<Sweet>> searchSweets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    );

    // Update sweet details by ID
    @PutMapping("/{id}")
    ResponseEntity<String> updateSweet(
            @PathVariable String id,
            @RequestBody Map<String, String> requestMap
    );

    // Delete sweet by ID (Admin only)
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteSweet(@PathVariable String id);

    // Purchase sweet - reduces quantity
    @PostMapping("/{id}/purchase")
    ResponseEntity<String> purchaseSweet(
            @PathVariable String id,
            @RequestBody Map<String, Integer> requestMap
    );

    // Restock sweet - increases quantity (Admin only)
    @PostMapping("/{id}/restock")
    ResponseEntity<String> restockSweet(
            @PathVariable String id,
            @RequestBody Map<String, Integer> requestMap
    );
}