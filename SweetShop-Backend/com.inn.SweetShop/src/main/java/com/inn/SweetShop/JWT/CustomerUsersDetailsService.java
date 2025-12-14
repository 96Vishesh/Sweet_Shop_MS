package com.inn.SweetShop.JWT;

import com.inn.SweetShop.POJO.User;
import com.inn.SweetShop.Dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Custom UserDetailsService implementation for Spring Security
 * Handles user authentication by loading user details from database
 */
@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    // Store the loaded user details for later use
    private User userDetail;

    /**
     * Load user by username (email in this case) for authentication
     * Called by Spring Security during login process
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername: {}", username);

        // Fetch user from database using email
        userDetail = userDao.findByEmailId(username);

        if (Objects.nonNull(userDetail)) {
            // Return Spring Security User object with email, password, and empty authorities
            return new org.springframework.security.core.userdetails.User(
                    userDetail.getEmail(),
                    userDetail.getPassword(),
                    new ArrayList<>()
            );
        } else {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
    }

    // Getter to access the loaded user details
    public User getUserDetail() {
        return userDetail;
    }
}