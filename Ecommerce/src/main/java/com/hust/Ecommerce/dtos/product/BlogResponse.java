package com.hust.Ecommerce.dtos.product;

import java.time.Instant;

import lombok.Data;

@Data
public class BlogResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private BlogResponse.UserResponse user;
    private String title;
    private String content;

    @Data
    public static class UserResponse {
        private Long id;
        private Instant createdAt;
        private Instant updatedAt;
        private String email;
        private String name;
    }
}
