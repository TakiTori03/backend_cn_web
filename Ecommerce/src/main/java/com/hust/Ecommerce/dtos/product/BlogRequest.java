package com.hust.Ecommerce.dtos.product;

import lombok.Data;

@Data
public class BlogRequest {

    private Long userId;
    private String title;
    private String content;
}
