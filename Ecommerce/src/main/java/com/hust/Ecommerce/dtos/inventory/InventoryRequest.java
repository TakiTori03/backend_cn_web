package com.hust.Ecommerce.dtos.inventory;

import org.springframework.lang.Nullable;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequest {
    @NotNull
    private Long variantId;
    private Integer available;
    @Nullable
    private Integer sold;

}
