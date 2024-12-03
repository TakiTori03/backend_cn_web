package com.hust.Ecommerce.dtos.product;

import java.time.Instant;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class VariantResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private VariantResponse.ProductResponse product;
    private String sku;
    private Double cost;
    private Double price;
    @Nullable
    private JsonNode properties;
    private Integer status;

    @Data
    public static class ProductResponse {
        private Long id;
        private Instant createdAt;
        private Instant updatedAt;
        private String name;
        private String slug;
    }
}
