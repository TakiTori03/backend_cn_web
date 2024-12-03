package com.hust.Ecommerce.dtos.product;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String slug;
    private String description;
    private String thumbnail;
}
