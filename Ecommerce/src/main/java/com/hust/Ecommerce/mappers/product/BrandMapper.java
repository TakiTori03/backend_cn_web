package com.hust.Ecommerce.mappers.product;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.product.BrandRequest;
import com.hust.Ecommerce.dtos.product.BrandResponse;
import com.hust.Ecommerce.entities.product.Brand;
import com.hust.Ecommerce.mappers.GenericMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BrandMapper extends GenericMapper<Brand, BrandRequest, BrandResponse> {

}
