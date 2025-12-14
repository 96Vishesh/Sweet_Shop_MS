package com.inn.SweetShop.ServiceImpl;

import com.inn.SweetShop.Constants.SweetConstants;
import com.inn.SweetShop.Dao.UserDao;
import com.inn.SweetShop.JWT.CustomerUsersDetailsService;
import com.inn.SweetShop.JWT.JwtFilter;
import com.inn.SweetShop.JWT.JwtUtil;
import com.inn.SweetShop.POJO.User;
import com.inn.SweetShop.Rest.UserRest;
import com.inn.SweetShop.utils.SweetUtils;
import lombok.extern.slf4j.Slf4j;
import com.inn.SweetShop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Service implementation for user operations
 * Handles business logic for authentication and user management
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register new user
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);

        try {
            if (validateSignUp(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));

                if (Objects.isNull(user)) {
                    // Create new user with encoded password
                    User newUser = getUserFromMap(requestMap);
                    newUser.setPassword(passwordEncoder.encode(requestMap.get("password")));
                    userDao.save(newUser);

                    return SweetUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
                } else {
                    return SweetUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                }
            } else {
                return SweetUtils.getResponseEntity(SweetConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Validate signup request contains all required fields
    private boolean validateSignUp(Map<String, String> requestMap) {
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password");
    }

    // Map request data to User object
    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false"); // Pending admin approval
        user.setRole("user");
        return user;
    }

    // Authenticate user and generate JWT token
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");

        try {
            // Authenticate with Spring Security
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );

            if (auth.isAuthenticated()) {
                // Check if user is approved by admin
                if (customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),
                                    customerUsersDetailsService.getUserDetail().getRole()) + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Wait for Admin Approval." + "\"}", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<String>("{\"message\":\"" + "Wrong Credentials" + "\"}", HttpStatus.UNAUTHORIZED);
    }

    // Validate JWT token
    @Override
    public ResponseEntity<String> checkToken() {
        return SweetUtils.getResponseEntity("true", HttpStatus.OK);
    }

    // Update user status (admin only)
    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));

                if (optional.isPresent()) {
                    userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return SweetUtils.getResponseEntity("User Status Successfully Updated", HttpStatus.OK);
                } else {
                    return SweetUtils.getResponseEntity("User id doesn't exist", HttpStatus.OK);
                }
            } else {
                return SweetUtils.getResponseEntity(SweetConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}