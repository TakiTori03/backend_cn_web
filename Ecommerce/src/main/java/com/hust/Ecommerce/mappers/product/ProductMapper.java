package com.hust.Ecommerce.mappers.product;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.product.ProductRequest;
import com.hust.Ecommerce.dtos.product.ProductResponse;
import com.hust.Ecommerce.entities.product.Product;
import com.hust.Ecommerce.mappers.GenericMapper;
import com.hust.Ecommerce.mappers.general.ImageMapper;
import com.hust.Ecommerce.util.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MapperUtils.class,
        ImageMapper.class, BrandMapper.class, CategoryMapper.class })
public interface ProductMapper extends GenericMapper<Product, ProductRequest, ProductResponse> {

    @Override
    @BeanMapping(qualifiedByName = "attachProduct")
    @Mapping(source = "categoryIds", target = "categories")
    @Mapping(source = "brandId", target = "brand")
    Product requestToEntity(ProductRequest request);

    @Override
    @BeanMapping(qualifiedByName = "attachProduct", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "categoryIds", target = "categories")
    @Mapping(source = "brandId", target = "brand")
    Product partialUpdate(@MappingTarget Product entity, ProductRequest request);

    @Override
    @Mapping(source = "brand", target = "brandProduct")
    ProductResponse entityToResponse(Product product);

}
