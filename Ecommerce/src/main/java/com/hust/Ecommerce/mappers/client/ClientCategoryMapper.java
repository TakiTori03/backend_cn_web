package com.hust.Ecommerce.mappers.client;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.hust.Ecommerce.dtos.client.ClientCategoryResponse;
import com.hust.Ecommerce.entities.product.Category;

@Component
public class ClientCategoryMapper {
    /**
     * Thông tin category gồm có name, slug, danh sách con cấp 1 và cha xa nhất (tạo
     * breadcrumb)
     */
    public ClientCategoryResponse entityToResponse(@Nullable Category category) {
        if (category == null) {
            return null;
        }

        ClientCategoryResponse categoryResponse = new ClientCategoryResponse();

        categoryResponse
                .setCategoryName(category.getName())
                .setCategorySlug(category.getSlug());
        return categoryResponse;
    }

}