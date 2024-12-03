package com.hust.Ecommerce.dtos.product;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VariantRequest {
    private Long productId;
    private String sku;
    private Double cost;
    private Double price;
    @Nullable
    private JsonNode properties;
    private Integer status;
}
