package com.hust.Ecommerce.dtos.client.cart;

import java.util.List;

import lombok.Data;

@Data
public class ClientCartResponse {
    private Long cartId;
    private List<ClientCartVariantResponse> cartItems;
}
