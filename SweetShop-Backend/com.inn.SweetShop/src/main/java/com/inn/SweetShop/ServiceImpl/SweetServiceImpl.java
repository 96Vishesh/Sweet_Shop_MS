package com.inn.SweetShop.ServiceImpl;

import com.inn.SweetShop.Constants.SweetConstants;
import com.inn.SweetShop.Dao.SweetDao;
import com.inn.SweetShop.JWT.JwtFilter;
import com.inn.SweetShop.POJO.Sweet;
import com.inn.SweetShop.Service.SweetService;
import com.inn.SweetShop.utils.SweetUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class SweetServiceImpl implements SweetService {

    @Autowired
    private SweetDao sweetDao;

    @Autowired
    private JwtFilter jwtFilter;

    // POST /api/sweets - Protected (Any authenticated user)
    @Override
    public ResponseEntity<String> addSweet(Map<String, String> requestMap) {
        try {
            // Check if user is authenticated
            if (!jwtFilter.isUser()) {
                return SweetUtils.getResponseEntity(SweetConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            if (validateSweetMap(requestMap, false)) {
                Sweet sweet = getSweetFromMap(requestMap, false);
                sweetDao.save(sweet);
                return SweetUtils.getResponseEntity("Sweet added successfully", HttpStatus.CREATED);
            }
            return SweetUtils.getResponseEntity(SweetConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            log.error("Duplicate sweet name: ", ex);
            return SweetUtils.getResponseEntity("Sweet with this name already exists", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Error in addSweet: ", ex);
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // GET /api/sweets - Protected (Any authenticated user)
    @Override
    public ResponseEntity<List<Sweet>> getAllSweets() {
        try {
            // Check if user is authenticated
            if (!jwtFilter.isUser()) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }

            List<Sweet> sweets = sweetDao.findAll();
            return new ResponseEntity<>(sweets, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error in getAllSweets: ", ex);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // GET /api/sweets/search - Protected (Any authenticated user)
    @Override
    public ResponseEntity<List<Sweet>> searchSweets(String name, String category,
                                                    BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            // Check if user is authenticated
            if (!jwtFilter.isUser()) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }

            List<Sweet> sweets = sweetDao.searchSweets(name, category, minPrice, maxPrice);
            return new ResponseEntity<>(sweets, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error in searchSweets: ", ex);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // PUT /api/sweets/:id - Protected (Any authenticated user)
    @Override
    public ResponseEntity<String> updateSweet(Long id, Map<String, String> requestMap) {
        try {
            // Check if user is authenticated
            if (!jwtFilter.isUser()) {
                return SweetUtils.getResponseEntity(SweetConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            if (validateSweetMap(requestMap, true)) {
                Optional<Sweet> optional = sweetDao.findById(id);
                if (optional.isPresent()) {
                    Sweet sweet = getSweetFromMap(requestMap, true);
                    sweet.setId(id);
                    sweetDao.save(sweet);
                    return SweetUtils.getResponseEntity("Sweet updated successfully", HttpStatus.OK);
                }
                return SweetUtils.getResponseEntity("Sweet not found", HttpStatus.NOT_FOUND);
            }
            return SweetUtils.getResponseEntity(SweetConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            log.error("Duplicate sweet name during update: ", ex);
            return SweetUtils.getResponseEntity("Sweet with this name already exists", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Error in updateSweet: ", ex);
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // DELETE /api/sweets/:id - Protected (Admin only)
    @Override
    public ResponseEntity<String> deleteSweet(Long id) {
        try {
            // Check if user is admin
            if (!jwtFilter.isAdmin()) {
                return SweetUtils.getResponseEntity(SweetConstants.UNAUTHORIZED_ACCESS, HttpStatus.FORBIDDEN);
            }

            Optional<Sweet> optional = sweetDao.findById(id);
            if (optional.isPresent()) {
                sweetDao.deleteById(id);
                return SweetUtils.getResponseEntity("Sweet deleted successfully", HttpStatus.OK);
            }
            return SweetUtils.getResponseEntity("Sweet not found", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Error in deleteSweet: ", ex);
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // POST /api/sweets/:id/purchase - Protected (Any authenticated user)
    @Override
    public ResponseEntity<String> purchaseSweet(Long id, Integer quantity) {
        try {
            // Check if user is authenticated
            if (!jwtFilter.isUser()) {
                return SweetUtils.getResponseEntity(SweetConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            Optional<Sweet> optional = sweetDao.findById(id);
            if (optional.isPresent()) {
                Sweet sweet = optional.get();
                if (sweet.getQuantity() >= quantity) {
                    sweet.setQuantity(sweet.getQuantity() - quantity);
                    sweetDao.save(sweet);
                    return SweetUtils.getResponseEntity("Sweet purchased successfully. Remaining quantity: " +
                            sweet.getQuantity(), HttpStatus.OK);
                }
                return SweetUtils.getResponseEntity("Insufficient stock. Available quantity: " +
                        sweet.getQuantity(), HttpStatus.BAD_REQUEST);
            }
            return SweetUtils.getResponseEntity("Sweet not found", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Error in purchaseSweet: ", ex);
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // POST /api/sweets/:id/restock - Protected (Admin only)
    @Override
    public ResponseEntity<String> restockSweet(Long id, Integer quantity) {
        try {
            // Check if user is admin
            if (!jwtFilter.isAdmin()) {
                return SweetUtils.getResponseEntity(SweetConstants.UNAUTHORIZED_ACCESS, HttpStatus.FORBIDDEN);
            }

            Optional<Sweet> optional = sweetDao.findById(id);
            if (optional.isPresent()) {
                Sweet sweet = optional.get();
                sweet.setQuantity(sweet.getQuantity() + quantity);
                sweetDao.save(sweet);
                return SweetUtils.getResponseEntity("Sweet restocked successfully. New quantity: " +
                        sweet.getQuantity(), HttpStatus.OK);
            }
            return SweetUtils.getResponseEntity("Sweet not found", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Error in restockSweet: ", ex);
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSweetMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name") &&
                requestMap.containsKey("category") &&
                requestMap.containsKey("price") &&
                requestMap.containsKey("quantity")) {
            return true;
        }
        return false;
    }

    private Sweet getSweetFromMap(Map<String, String> requestMap, boolean isUpdate) {
        Sweet sweet = new Sweet();
        sweet.setName(requestMap.get("name"));
        sweet.setCategory(requestMap.get("category"));
        sweet.setPrice(new BigDecimal(requestMap.get("price")));
        sweet.setQuantity(Integer.parseInt(requestMap.get("quantity")));
        if (requestMap.containsKey("description")) {
            sweet.setDescription(requestMap.get("description"));
        }
        return sweet;
    }
}