package com.hust.Ecommerce.dtos.client.cart;

import lombok.Data;

@Data
public class ClientCartVariantRequest {
    private Long variantId;
    private Integer quantity;
}
