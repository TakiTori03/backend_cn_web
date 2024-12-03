package com.hust.Ecommerce.mappers.inventory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.inventory.InventoryRequest;
import com.hust.Ecommerce.dtos.inventory.InventoryResponse;
import com.hust.Ecommerce.entities.inventory.Inventory;
import com.hust.Ecommerce.mappers.GenericMapper;
import com.hust.Ecommerce.util.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface InventoryMapper extends GenericMapper<Inventory, InventoryRequest, InventoryResponse> {

    @Override
    @Mapping(source = "variantId", target = "variant")
    Inventory requestToEntity(InventoryRequest request);

}
