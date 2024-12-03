package com.hust.Ecommerce.mappers.authentication;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.authentication.UserRequest;
import com.hust.Ecommerce.dtos.authentication.UserResponse;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.mappers.GenericMapper;
import com.hust.Ecommerce.util.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MapperUtils.class })
public interface UserMapper extends GenericMapper<User, UserRequest, UserResponse> {

    @Override
    @BeanMapping(qualifiedByName = "attachUser")
    @Mapping(source = "password", target = "password", qualifiedByName = "hashPassword")
    User requestToEntity(UserRequest request);

    @Override
    @BeanMapping(qualifiedByName = "attachUser")
    @Mapping(source = "password", target = "password", qualifiedByName = "hashPassword", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(@MappingTarget User entity, UserRequest request);

    // @BeanMapping(nullValuePropertyMappingStrategy =
    // NullValuePropertyMappingStrategy.IGNORE)
    // User partialUpdate(@MappingTarget User entity,
    // ClientPersonalSettingUserRequest request);

}
