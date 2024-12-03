package com.hust.Ecommerce.mappers.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleProductInventory {
    private Long productId;
    private Integer available;
    private Integer sold;

    public SimpleProductInventory(
            Long productId,
            Long available,
            Long sold) {
        this.productId = productId;
        this.available = Math.toIntExact(available);
        this.sold = Math.toIntExact(sold);
    }
}
