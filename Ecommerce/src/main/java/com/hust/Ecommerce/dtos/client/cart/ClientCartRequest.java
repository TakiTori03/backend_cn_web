package com.hust.Ecommerce.dtos.client.cart;

import java.util.List;

import org.springframework.lang.Nullable;

import lombok.Data;

@Data
public class ClientCartRequest {
    @Nullable
    private Long cartId;
    private Long userId;
    private List<ClientCartVariantRequest> cartItems;
    private Integer status;
    private UpdateQuantityType updateQuantityType;
}
