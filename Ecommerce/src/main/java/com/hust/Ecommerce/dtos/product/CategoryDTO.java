package com.hust.Ecommerce.dtos.product;

import com.hust.Ecommerce.constants.MessageKeys;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {
    @NotEmpty(message = MessageKeys.CATEGORIES_NAME_REQUIRED)
    private String name;
}
