package com.inn.SweetShop.Rest;

import com.inn.SweetShop.POJO.Sweet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/sweets")
public interface SweetRest {

    @PostMapping
    ResponseEntity<String> addSweet(@RequestBody Map<String, String> requestMap);

    @GetMapping
    ResponseEntity<List<Sweet>> getAllSweets();

    @GetMapping("/search")
    ResponseEntity<List<Sweet>> searchSweets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    );

    @PutMapping("/{id}")
    ResponseEntity<String> updateSweet(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestMap
    );

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteSweet(@PathVariable Long id);

    @PostMapping("/{id}/purchase")
    ResponseEntity<String> purchaseSweet(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> requestMap
    );

    @PostMapping("/{id}/restock")
    ResponseEntity<String> restockSweet(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> requestMap
    );
}
