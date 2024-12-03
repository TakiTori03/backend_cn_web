package com.hust.Ecommerce.dtos.product;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.hust.Ecommerce.dtos.general.ImageRequest;

import lombok.Data;

@Data
public class ProductRequest {

    private String name;
    private String slug;
    private String description;
    private List<ImageRequest> images;
    private Integer status;

    private List<Long> categoryIds;

    private Long brandId;

    private String unit;

    private Double weight;

    private JsonNode specifications;

    private Long warrantyDuration;

    private List<VariantRequest> variants;
}
