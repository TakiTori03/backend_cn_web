package com.hust.Ecommerce.mappers.product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.product.BlogRequest;
import com.hust.Ecommerce.dtos.product.BlogResponse;
import com.hust.Ecommerce.entities.product.Blog;
import com.hust.Ecommerce.mappers.GenericMapper;
import com.hust.Ecommerce.util.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface BlogMapper extends GenericMapper<Blog, BlogRequest, BlogResponse> {

    @Override
    @Mapping(source = "userId", target = "user")
    Blog requestToEntity(BlogRequest request);
}