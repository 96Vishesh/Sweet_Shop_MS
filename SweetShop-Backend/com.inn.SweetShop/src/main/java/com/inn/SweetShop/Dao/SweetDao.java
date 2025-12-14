package com.inn.SweetShop.Dao;

import com.inn.SweetShop.POJO.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SweetDao extends JpaRepository<Sweet, String> {

    // Search by name (case-insensitive)
    List<Sweet> findByNameContainingIgnoreCase(String name);

    // Search by category (case-insensitive)
    List<Sweet> findByCategoryContainingIgnoreCase(String category);

    // Search by price range
    List<Sweet> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // ✅ FIXED Combined Search (PostgreSQL-safe)
    @Query(
            value = """
            SELECT *
            FROM sweets
            WHERE (:name IS NULL OR name ILIKE '%' || :name || '%')
              AND (:category IS NULL OR category ILIKE '%' || :category || '%')
              AND (:minPrice IS NULL OR price >= :minPrice)
              AND (:maxPrice IS NULL OR price <= :maxPrice)
        """,
            nativeQuery = true
    )
    List<Sweet> searchSweets(
            @Param("name") String name,
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );
    @Query(value = "SELECT id FROM sweets ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String getLastSweetId();
}
