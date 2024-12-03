package com.hust.Ecommerce.mappers.product;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.product.CategoryRequest;
import com.hust.Ecommerce.dtos.product.CategoryResponse;
import com.hust.Ecommerce.entities.product.Category;
import com.hust.Ecommerce.mappers.GenericMapper;
import com.hust.Ecommerce.util.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface CategoryMapper extends GenericMapper<Category, CategoryRequest, CategoryResponse> {

}
