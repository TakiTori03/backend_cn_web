package com.hust.Ecommerce.dtos.product;

import java.time.Instant;
import java.util.List;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.hust.Ecommerce.dtos.general.ImageResponse;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String slug;

    private String description;
    private List<ImageResponse> images;
    private Integer status;

    private List<CategoryResponse> categories;

    private BrandResponse brandProduct;

    private String unit;

    private JsonNode specifications;

    private Double weight;

    private Long warrantyDuration;

    private List<ProductResponse.VariantResponse> variants;

    @Data
    public static class VariantResponse {
        private Long id;
        private Instant createdAt;
        private Instant updatedAt;
        private String sku;
        private Double cost;
        @Nullable
        private JsonNode properties;
        private Integer status;
    }
}
