package com.hust.Ecommerce.dtos.inventory;

import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class InventoryResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private InventoryResponse.VariantInventoryResponse variant;
    private Integer available;
    private Integer sold;

    @Data
    public static class VariantInventoryResponse {
        private Long id;
        private String sku;
        private JsonNode properties;

    }
}
