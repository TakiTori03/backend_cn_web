package com.hust.Ecommerce.mappers.review;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.review.ReviewRequest;
import com.hust.Ecommerce.dtos.review.ReviewResponse;
import com.hust.Ecommerce.entities.review.Review;
import com.hust.Ecommerce.mappers.GenericMapper;
import com.hust.Ecommerce.util.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface ReviewMapper extends GenericMapper<Review, ReviewRequest, ReviewResponse> {

    @Override
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "productId", target = "product")
    Review requestToEntity(ReviewRequest request);

    @Override
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "productId", target = "product")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Review partialUpdate(@MappingTarget Review entity, ReviewRequest request);

}
