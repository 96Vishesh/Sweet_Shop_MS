package com.inn.SweetShop.Wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper class for User data transfer
 * Used to send user information without exposing sensitive fields like password
 */
@Data
@NoArgsConstructor
public class UserWrapper {

    private Integer id;
    private String name;
    private String email;
    private String contactNumber;
    private String status;

    // Constructor for creating UserWrapper with all fields
    public UserWrapper(Integer id, String name, String email, String contactNumber, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.status = status;
    }
}