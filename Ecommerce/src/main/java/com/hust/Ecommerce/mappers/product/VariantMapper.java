package com.hust.Ecommerce.mappers.product;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.product.VariantRequest;
import com.hust.Ecommerce.dtos.product.VariantResponse;

import com.hust.Ecommerce.entities.product.Variant;
import com.hust.Ecommerce.mappers.GenericMapper;
import com.hust.Ecommerce.util.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface VariantMapper extends GenericMapper<Variant, VariantRequest, VariantResponse> {

    @Override
    @BeanMapping(qualifiedByName = "attachProduct")
    @Mapping(source = "productId", target = "product")
    Variant requestToEntity(VariantRequest request);

    @Override
    @BeanMapping(qualifiedByName = "attachProduct", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "productId", target = "product")
    Variant partialUpdate(@MappingTarget Variant entity, VariantRequest request);

    @Override
    VariantResponse entityToResponse(Variant entity);

}
