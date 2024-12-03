package com.hust.Ecommerce.dtos.chat;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String content;
    private Integer status;
    private UserResponse user;

    @Data
    @AllArgsConstructor
    public static class UserResponse {
        private Long id;
        private String name;
        private String email;
    }
}
