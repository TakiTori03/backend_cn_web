package com.hust.Ecommerce.dtos.client.order;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderVariantRequest {
    private Long variantId;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal amount;
}
