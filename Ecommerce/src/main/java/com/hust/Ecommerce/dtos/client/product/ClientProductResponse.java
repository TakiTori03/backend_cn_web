package com.hust.Ecommerce.dtos.client.product;

import java.util.List;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.hust.Ecommerce.dtos.client.ClientCategoryResponse;
import com.hust.Ecommerce.dtos.general.ImageResponse;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientProductResponse {
    private Long productId;
    private String productName;
    private String productSlug;
    @Nullable
    private String productDescription;
    private List<ImageResponse> productImages;
    @Nullable
    private List<ClientCategoryResponse> productCategories;
    @Nullable
    private ClientBrandResponse productBrand;
    @Nullable
    private JsonNode productSpecifications;
    private List<ClientVariantResponse> productVariants;

    private Long productWarrantyDuration;
    private boolean productSaleable;
    private double productAverageRate;
    private int productCountReviews;

    @Data
    @Accessors(chain = true)
    public static class ClientBrandResponse {
        private Long brandId;
        private String brandName;
    }

    @Data
    @Accessors(chain = true)
    public static class ClientVariantResponse {
        private Long variantId;
        private Double variantPrice;
        @Nullable
        private JsonNode variantProperties;
        private Integer variantAvailable;
    }
}
