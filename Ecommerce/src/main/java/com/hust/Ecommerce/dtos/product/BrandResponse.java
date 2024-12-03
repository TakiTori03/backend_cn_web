package com.hust.Ecommerce.dtos.product;

import java.time.Instant;

import lombok.Data;

@Data
public class BrandResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String description;

}
