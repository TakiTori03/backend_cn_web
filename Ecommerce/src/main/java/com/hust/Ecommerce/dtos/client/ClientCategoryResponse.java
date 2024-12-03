package com.hust.Ecommerce.dtos.client;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientCategoryResponse {
    private String categoryName;
    private String categorySlug;
}
