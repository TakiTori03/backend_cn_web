package com.hust.Ecommerce.dtos.authentication;

import java.time.Instant;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String email;
    private String name;
    private String phoneNumber;
    private String gender;
    private String status;
    private String address;
    private String avatar;
    private String language;
    private RoleDTO role;
}