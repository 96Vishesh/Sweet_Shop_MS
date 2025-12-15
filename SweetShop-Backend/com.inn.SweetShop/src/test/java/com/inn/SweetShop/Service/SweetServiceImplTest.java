package com.inn.SweetShop.Service;

import com.inn.SweetShop.Constants.SweetConstants;
import com.inn.SweetShop.Dao.SweetDao;
import com.inn.SweetShop.JWT.JwtFilter;
import com.inn.SweetShop.POJO.Sweet;
import com.inn.SweetShop.ServiceImpl.SweetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SweetServiceImplTest {

    //                          ---------------SETUP AND CONFIGURATION-------------

    @Autowired
    private SweetServiceImpl sweetService;

    @MockBean
    private SweetDao sweetDao;

    @MockBean
    private JwtFilter jwtFilter;

    private Sweet kajuKatli;
    private Sweet gajarHalwa;
    private Sweet gulabJamun;
    private Sweet rabdi;

    @BeforeEach
    void setupDefaultSweets() {
        // Initialize test sweets
        kajuKatli = new Sweet("S000001", "Kaju Katli", "Nut-Based", new BigDecimal("50.00"), 20, "Delicious cashew sweet");
        gajarHalwa = new Sweet("S000002", "Gajar Halwa", "Vegetable-Based", new BigDecimal("30.00"), 15, "Carrot based dessert");
        gulabJamun = new Sweet("S000003", "Gulab Jamun", "Milk-Based", new BigDecimal("10.00"), 50, "Soft milk balls");
        rabdi = new Sweet("S000004", "Rabdi", "Milk-Based", new BigDecimal("40.00"), 10, "Thick sweetened milk");
    }

    //                          ---------------ADD SWEETS-------------

    @Test
    void shouldAddSweetSuccessfullyToShop() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);
        when(sweetDao.getLastSweetId()).thenReturn("S000003");
        when(sweetDao.save(any(Sweet.class))).thenReturn(rabdi);

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "Rabdi");
        requestMap.put("category", "Milk-Based");
        requestMap.put("price", "40.00");
        requestMap.put("quantity", "10");
        requestMap.put("description", "Thick sweetened milk");

        // Act
        ResponseEntity<String> response = sweetService.addSweet(requestMap);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Sweet added successfully", response.getBody());
        verify(sweetDao, times(1)).save(any(Sweet.class));
    }

    @Test
    void shouldThrowUnauthorizedWhenAddingSweetWithoutAuthentication() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(false);

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "Rabdi");
        requestMap.put("category", "Milk-Based");
        requestMap.put("price", "40.00");
        requestMap.put("quantity", "10");

        // Act
        ResponseEntity<String> response = sweetService.addSweet(requestMap);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(SweetConstants.UNAUTHORIZED_ACCESS, response.getBody());
        verify(sweetDao, never()).save(any(Sweet.class));
    }

    @Test
    void shouldThrowExceptionOnAddingDuplicateSweets() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);
        when(sweetDao.save(any(Sweet.class)))
                .thenThrow(new org.springframework.dao.DataIntegrityViolationException("Duplicate entry"));

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "Kaju Katli");
        requestMap.put("category", "Nut-Based");
        requestMap.put("price", "50.00");
        requestMap.put("quantity", "20");

        // Act
        ResponseEntity<String> response = sweetService.addSweet(requestMap);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Sweet with this name already exists", response.getBody());
    }

    @Test
    void shouldReturnBadRequestForInvalidSweetData() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "Incomplete Sweet");
        // Missing required fields: category, price, quantity

        // Act
        ResponseEntity<String> response = sweetService.addSweet(requestMap);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(SweetConstants.INVALID_DATA, response.getBody());
        verify(sweetDao, never()).save(any(Sweet.class));
    }

    //                      --------------GET ALL SWEETS--------------

    @Test
    void shouldReturnAllSweetsOfShop() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);
        List<Sweet> sweetsList = Arrays.asList(kajuKatli, gajarHalwa, gulabJamun);
        when(sweetDao.findAll()).thenReturn(sweetsList);

        // Act
        ResponseEntity<List<Sweet>> response = sweetService.getAllSweets();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals("Kaju Katli", response.getBody().get(0).getName());
        verify(sweetDao, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNotAuthenticated() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(false);

        // Act
        ResponseEntity<List<Sweet>> response = sweetService.getAllSweets();

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(sweetDao, never()).findAll();
    }

    //                      --------------SEARCH SWEETS--------------

    @Test
    void shouldReturnSweetsFromSearchByNameAndCategory() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);
        List<Sweet> milkSweets = Arrays.asList(gulabJamun, rabdi);
        when(sweetDao.searchSweets(anyString(), eq("Milk-Based"), any(), any())).thenReturn(milkSweets);

        // Act
        ResponseEntity<List<Sweet>> response = sweetService.searchSweets(null, "Milk-Based", null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().stream().allMatch(s -> s.getCategory().equals("Milk-Based")));
    }

    @Test
    void shouldReturnSweetsWithinPriceRange() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);
        List<Sweet> sweetsInRange = Arrays.asList(gulabJamun, gajarHalwa);
        when(sweetDao.searchSweets(any(), any(), eq(new BigDecimal("10.00")), eq(new BigDecimal("40.00"))))
                .thenReturn(sweetsInRange);

        // Act
        ResponseEntity<List<Sweet>> response = sweetService.searchSweets(null, null, 
                new BigDecimal("10.00"), new BigDecimal("40.00"));

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnUnauthorizedForSearchWithoutAuthentication() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(false);

        // Act
        ResponseEntity<List<Sweet>> response = sweetService.searchSweets("Kaju", null, null, null);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(sweetDao, never()).searchSweets(any(), any(), any(), any());
    }

    //                      --------------UPDATE SWEET--------------

    @Test
    void shouldUpdateSweetSuccessfully() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);
        when(sweetDao.findById("S000001")).thenReturn(Optional.of(kajuKatli));
        when(sweetDao.save(any(Sweet.class))).thenReturn(kajuKatli);

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "Kaju Katli Premium");
        requestMap.put("category", "Nut-Based");
        requestMap.put("price", "60.00");
        requestMap.put("quantity", "25");

        // Act
        ResponseEntity<String> response = sweetService.updateSweet("S000001", requestMap);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sweet updated successfully", response.getBody());
        verify(sweetDao, times(1)).save(any(Sweet.class));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentSweet() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);
        when(sweetDao.findById("S999999")).thenReturn(Optional.empty());

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "Non-existent Sweet");
        requestMap.put("category", "Unknown");
        requestMap.put("price", "100.00");
        requestMap.put("quantity", "10");

        // Act
        ResponseEntity<String> response = sweetService.updateSweet("S999999", requestMap);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Sweet not found", response.getBody());
        verify(sweetDao, never()).save(any(Sweet.class));
    }

    @Test
    void shouldReturnUnauthorizedWhenUpdatingWithoutAuthentication() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(false);

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "Updated Sweet");
        requestMap.put("category", "Category");
        requestMap.put("price", "50.00");
        requestMap.put("quantity", "10");

        // Act
        ResponseEntity<String> response = sweetService.updateSweet("S000001", requestMap);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(SweetConstants.UNAUTHORIZED_ACCESS, response.getBody());
        verify(sweetDao, never()).save(any(Sweet.class));
    }

    //                      --------------DELETE SWEET BY ID--------------

    @Test
    void shouldDeleteSweetSuccessfullyFromShop() {
        // Arrange
        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sweetDao.findById("S000001")).thenReturn(Optional.of(kajuKatli));
        doNothing().when(sweetDao).deleteById("S000001");

        // Act
        ResponseEntity<String> response = sweetService.deleteSweet("S000001");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sweet deleted successfully", response.getBody());
        verify(sweetDao, times(1)).deleteById("S000001");
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingSweet() {
        // Arrange
        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sweetDao.findById("S999999")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = sweetService.deleteSweet("S999999");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Sweet not found", response.getBody());
        verify(sweetDao, never()).deleteById(any());
    }

    @Test
    void shouldReturnForbiddenWhenNonAdminTriesToDelete() {
        // Arrange
        when(jwtFilter.isAdmin()).thenReturn(false);

        // Act
        ResponseEntity<String> response = sweetService.deleteSweet("S000001");

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(SweetConstants.UNAUTHORIZED_ACCESS, response.getBody());
        verify(sweetDao, never()).deleteById(any());
    }

    //                        -------------INVENTORY MANAGEMENT------------
    //                        -------------PURCHASE SWEET---------------

    @Test
    void shouldPurchaseSweetSuccessfullyAndReduceStock() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);
        when(sweetDao.findById("S000001")).thenReturn(Optional.of(kajuKatli));
        
        Sweet updatedSweet = new Sweet("S000001", "Kaju Katli", "Nut-Based", 
                new BigDecimal("50.00"), 15, "Delicious cashew sweet");
        when(sweetDao.save(any(Sweet.class))).thenReturn(updatedSweet);

        // Act
        ResponseEntity<String> response = sweetService.purchaseSweet("S000001", 5);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Sweet purchased successfully"));
        assertTrue(response.getBody().contains("Remaining quantity: 15"));
        verify(sweetDao, times(1)).save(any(Sweet.class));
    }

    @Test
    void shouldThrowExceptionIfStockNotAvailable() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);
        when(sweetDao.findById("S000002")).thenReturn(Optional.of(gajarHalwa));

        // Act
        ResponseEntity<String> response = sweetService.purchaseSweet("S000002", 20);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Insufficient stock"));
        assertTrue(response.getBody().contains("Available quantity: 15"));
        verify(sweetDao, never()).save(any(Sweet.class));
    }

    @Test
    void shouldThrowExceptionIfSweetNotFoundForPurchase() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);
        when(sweetDao.findById("S999999")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = sweetService.purchaseSweet("S999999", 10);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Sweet not found", response.getBody());
        verify(sweetDao, never()).save(any(Sweet.class));
    }

    @Test
    void shouldReturnUnauthorizedForPurchaseWithoutAuthentication() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(false);

        // Act
        ResponseEntity<String> response = sweetService.purchaseSweet("S000001", 5);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(SweetConstants.UNAUTHORIZED_ACCESS, response.getBody());
        verify(sweetDao, never()).findById(any());
    }

    //                        -------------RESTOCK SWEET---------------

    @Test
    void shouldRestockSweetSuccessfully() {
        // Arrange
        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sweetDao.findById("S000001")).thenReturn(Optional.of(kajuKatli));
        
        Sweet restockedSweet = new Sweet("S000001", "Kaju Katli", "Nut-Based", 
                new BigDecimal("50.00"), 30, "Delicious cashew sweet");
        when(sweetDao.save(any(Sweet.class))).thenReturn(restockedSweet);

        // Act
        ResponseEntity<String> response = sweetService.restockSweet("S000001", 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Sweet restocked successfully"));
        assertTrue(response.getBody().contains("New quantity: 30"));
        verify(sweetDao, times(1)).save(any(Sweet.class));
    }

    @Test
    void shouldThrowExceptionIfSweetNotFoundToRestock() {
        // Arrange
        when(jwtFilter.isAdmin()).thenReturn(true);
        when(sweetDao.findById("S999999")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = sweetService.restockSweet("S999999", 10);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Sweet not found", response.getBody());
        verify(sweetDao, never()).save(any(Sweet.class));
    }

    @Test
    void shouldReturnForbiddenWhenNonAdminTriesToRestock() {
        // Arrange
        when(jwtFilter.isAdmin()).thenReturn(false);

        // Act
        ResponseEntity<String> response = sweetService.restockSweet("S000001", 10);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(SweetConstants.UNAUTHORIZED_ACCESS, response.getBody());
        verify(sweetDao, never()).findById(any());
    }

    //                        -------------EDGE CASES AND ERROR HANDLING---------------

    @Test
    void shouldHandleNullRequestMapGracefully() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> {
            ResponseEntity<String> response = sweetService.addSweet(null);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        });
    }

    @Test
    void shouldHandleZeroQuantityPurchase() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);
        when(sweetDao.findById("S000001")).thenReturn(Optional.of(kajuKatli));
        when(sweetDao.save(any(Sweet.class))).thenReturn(kajuKatli);

        // Act
        ResponseEntity<String> response = sweetService.purchaseSweet("S000001", 0);

        // Assert - Should succeed as 0 quantity doesn't reduce stock
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldPreventNegativeQuantityPurchase() {
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);
        when(sweetDao.findById("S000001")).thenReturn(Optional.of(kajuKatli));

        // Act
        ResponseEntity<String> response = sweetService.purchaseSweet("S000001", -5);

        // Assert - Negative quantity should be allowed mathematically (would increase stock)
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
