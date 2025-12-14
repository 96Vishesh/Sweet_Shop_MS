package com.inn.SweetShop.Service;
import com.inn.SweetShop.POJO.Sweet;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SweetService {

    ResponseEntity<String> addSweet(Map<String, String> requestMap);

    ResponseEntity<List<Sweet>> getAllSweets();

    ResponseEntity<List<Sweet>> searchSweets(String name, String category,
                                             BigDecimal minPrice, BigDecimal maxPrice);

    ResponseEntity<String> updateSweet(Long id, Map<String, String> requestMap);

    ResponseEntity<String> deleteSweet(Long id);

    ResponseEntity<String> purchaseSweet(Long id, Integer quantity);

    ResponseEntity<String> restockSweet(Long id, Integer quantity);
}