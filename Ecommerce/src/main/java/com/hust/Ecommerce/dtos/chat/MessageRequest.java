package com.hust.Ecommerce.dtos.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageRequest {
    private String content;
    private Integer status;
    private Long userId;
    private Long roomId;
}
