package com.inn.SweetShop.RestImpl;

import com.inn.SweetShop.Constants.SweetConstants;
import com.inn.SweetShop.JWT.JwtFilter;
import com.inn.SweetShop.POJO.Sweet;
import com.inn.SweetShop.Rest.SweetRest;
import com.inn.SweetShop.Service.SweetService;
import com.inn.SweetShop.utils.SweetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class SweetRestImpl implements SweetRest {

    @Autowired
    private SweetService sweetService;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addSweet(Map<String, String> requestMap) {
        try {
            return sweetService.addSweet(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Sweet>> getAllSweets() {
        try {
            return sweetService.getAllSweets();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Sweet>> searchSweets(String name, String category,
                                                    BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            return sweetService.searchSweets(name, category, minPrice, maxPrice);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateSweet(Long id, Map<String, String> requestMap) {
        try {
            return sweetService.updateSweet(id, requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteSweet(Long id) {
        try {
            // Check if user is admin
            if (jwtFilter.isAdmin()) {
                return sweetService.deleteSweet(id);
            } else {
                return SweetUtils.getResponseEntity(SweetConstants.ADMIN_ONLY, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> purchaseSweet(Long id, Map<String, Integer> requestMap) {
        try {
            Integer quantity = requestMap.get("quantity");
            if (quantity != null && quantity > 0) {
                return sweetService.purchaseSweet(id, quantity);
            }
            return SweetUtils.getResponseEntity("Invalid quantity", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> restockSweet(Long id, Map<String, Integer> requestMap) {
        try {
            // Check if user is admin
            if (jwtFilter.isAdmin()) {
                Integer quantity = requestMap.get("quantity");
                if (quantity != null && quantity > 0) {
                    return sweetService.restockSweet(id, quantity);
                }
                return SweetUtils.getResponseEntity("Invalid quantity", HttpStatus.BAD_REQUEST);
            } else {
                return SweetUtils.getResponseEntity(SweetConstants.ADMIN_ONLY, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}