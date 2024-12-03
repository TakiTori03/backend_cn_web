package com.hust.Ecommerce.dtos.client.order;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.JsonNode;

import io.micrometer.common.lang.Nullable;
import lombok.Data;

@Data
public class ClientOrderVariantResponse {
    private ClientVariantResponse orderItemVariant;
    private BigDecimal orderItemPrice;
    private Integer orderItemQuantity;
    private BigDecimal orderItemAmount;

    @Data
    public static class ClientVariantResponse {
        private Long variantId;
        private ClientProductResponse variantProduct;
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