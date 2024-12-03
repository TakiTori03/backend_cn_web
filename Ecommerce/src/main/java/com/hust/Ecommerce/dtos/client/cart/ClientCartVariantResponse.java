package com.hust.Ecommerce.dtos.client.cart;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class ClientCartVariantResponse {
    private ClientVariantResponse cartItemVariant;
    private Integer cartItemQuantity;

    @Data
    public static class ClientVariantResponse {
        private Long variantId;
        private ClientProductResponse variantProduct;
        private Double variantPrice;
        @Nullable
        private JsonNode variantProperties;

        @Data
        public static class ClientProductResponse {
            private Long productId;
            private String productName;
            private String productSlug;
            @Nullable
            private String productThumbnail;
        }
    }
}
