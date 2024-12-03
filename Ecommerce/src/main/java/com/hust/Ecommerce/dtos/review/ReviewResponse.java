package com.hust.Ecommerce.dtos.review;

import java.time.Instant;

import org.springframework.lang.Nullable;

import lombok.Data;

@Data
public class ReviewResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private ReviewResponse.UserResponse user;
    private ReviewResponse.ProductReviewResponse product;
    private Integer rate;
    private String content;
    @Nullable
    private String reply;

    @Data
    public static class UserResponse {
        private Long id;
        private Instant createdAt;
        private Instant updatedAt;
        private String email;
        private String name;
    }

    @Data
    public static class ProductReviewResponse {
        private Long id;
        private Instant createdAt;
        private Instant updatedAt;
        private String name;
        private String slug;
    }
}
