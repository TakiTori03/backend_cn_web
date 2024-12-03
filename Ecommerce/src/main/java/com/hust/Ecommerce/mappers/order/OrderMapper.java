package com.hust.Ecommerce.mappers.order;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.client.order.OrderRequest;
import com.hust.Ecommerce.dtos.client.order.OrderResponse;
import com.hust.Ecommerce.entities.order.Order;
import com.hust.Ecommerce.mappers.GenericMapper;
import com.hust.Ecommerce.mappers.authentication.UserMapper;
import com.hust.Ecommerce.util.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MapperUtils.class,
        UserMapper.class, OrderVariantMapper.class })
public interface OrderMapper extends GenericMapper<Order, OrderRequest, OrderResponse> {

    @Override
    @BeanMapping(qualifiedByName = "attachOrder")
    @Mapping(source = "userId", target = "user")
    Order requestToEntity(OrderRequest request);

    @Override
    @BeanMapping(qualifiedByName = "attachOrder", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "userId", target = "user")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "orderVariants", ignore = true)
    Order partialUpdate(@MappingTarget Order entity, OrderRequest request);

}
