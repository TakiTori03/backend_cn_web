package com.hust.Ecommerce.mappers.chat;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.chat.RoomRequest;
import com.hust.Ecommerce.dtos.chat.RoomResponse;
import com.hust.Ecommerce.entities.chat.Room;
import com.hust.Ecommerce.mappers.GenericMapper;
import com.hust.Ecommerce.util.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface RoomMapper extends GenericMapper<Room, RoomRequest, RoomResponse> {
    @Override
    @Mapping(source = "userId", target = "user")
    Room requestToEntity(RoomRequest request);
}
