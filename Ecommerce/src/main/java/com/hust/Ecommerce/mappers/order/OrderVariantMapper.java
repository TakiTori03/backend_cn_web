package com.hust.Ecommerce.mappers.order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.client.order.OrderVariantRequest;
import com.hust.Ecommerce.dtos.client.order.OrderVariantResponse;
import com.hust.Ecommerce.entities.order.OrderVariant;
import com.hust.Ecommerce.mappers.GenericMapper;
import com.hust.Ecommerce.util.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface OrderVariantMapper extends GenericMapper<OrderVariant, OrderVariantRequest, OrderVariantResponse> {

    @Override
    @Mapping(source = "variantId", target = "variant")
    OrderVariant requestToEntity(OrderVariantRequest request);

    @Override
    @Mapping(source = "variantId", target = "variant")
    OrderVariant partialUpdate(@MappingTarget OrderVariant entity, OrderVariantRequest request);

}
