package com.hust.Ecommerce.dtos.authentication;

import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String gender;
    private String status;
    private String address;
    private String avatar;
    private String language;
    private String role;

}
