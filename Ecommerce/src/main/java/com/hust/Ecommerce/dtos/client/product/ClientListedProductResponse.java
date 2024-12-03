package com.hust.Ecommerce.dtos.client.product;

import java.util.List;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientListedProductResponse {
    private Long productId;
    private String productName;
    private String productSlug;
    @Nullable
    private String productThumbnail;
    // san pham co the ban hay khong
    private boolean productSaleable;
    private List<Double> productPriceRange;
    private List<ClientListedVariantResponse> productVariants;

    private double productAverageRate;
    private int productSold;

    @Data
    @Accessors(chain = true)
    public static class ClientListedVariantResponse {
        private Long variantId;
        private Double variantPrice;
        @Nullable
        private JsonNode variantProperties;
    }
}
