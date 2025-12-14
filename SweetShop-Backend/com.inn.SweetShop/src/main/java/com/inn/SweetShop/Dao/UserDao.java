package com.inn.SweetShop.Dao;

import com.inn.SweetShop.POJO.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for User entity operations
 */
@EnableJpaRepositories
@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    // Find user by email ID
    User findByEmailId(@Param("email") String email);

    // Update user status by ID
    @Transactional
    @Modifying
    @Query
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    // Find user by email
    User findByEmail(String email);
}