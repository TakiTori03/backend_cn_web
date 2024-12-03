package com.hust.Ecommerce.dtos.client.review;

import java.time.Instant;

import org.springframework.lang.Nullable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientSimpleReviewResponse {
    private Long reviewId;
    private Instant reviewCreatedAt;
    private Instant reviewUpdatedAt;
    private ClientSimpleReviewResponse.UserResponse reviewUser;
    private Integer reviewRate;
    private String reviewContent;
    @Nullable
    private String reviewReply;

    @Data
    @Accessors(chain = true)
    public static class UserResponse {
        private Long userId;
        private String userEmail;
        private String userName;
    }
}