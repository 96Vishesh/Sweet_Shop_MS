package com.inn.SweetShop.RestImpl;

import com.inn.SweetShop.JWT.JwtFilter;
import com.inn.SweetShop.Constants.SweetConstants;
import com.inn.SweetShop.Dao.UserDao;
import com.inn.SweetShop.Rest.UserRest;
import com.inn.SweetShop.Service.UserService;
import com.inn.SweetShop.utils.SweetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * REST controller implementation for user authentication endpoints
 */
@RestController
@RequestMapping("/api/auth")
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDao userDao;

    // Handle user registration
    @Override
    public ResponseEntity<String> signUp(@RequestBody Map<String, String> requestMap) {
        try {
            return userService.signUp(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle user login
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle user profile update
    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            return userService.update(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Validate JWT token
    @Override
    public ResponseEntity<String> checkToken() {
        try {
            return userService.checkToken();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SweetUtils.getResponseEntity(SweetConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}