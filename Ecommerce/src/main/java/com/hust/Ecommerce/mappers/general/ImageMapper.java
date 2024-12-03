package com.hust.Ecommerce.mappers.general;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.general.ImageRequest;
import com.hust.Ecommerce.dtos.general.ImageResponse;
import com.hust.Ecommerce.entities.general.Image;
import com.hust.Ecommerce.mappers.GenericMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper extends GenericMapper<Image, ImageRequest, ImageResponse> {

}
