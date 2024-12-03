package com.hust.Ecommerce.mappers.chat;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.chat.MessageRequest;
import com.hust.Ecommerce.dtos.chat.MessageResponse;
import com.hust.Ecommerce.entities.chat.Message;
import com.hust.Ecommerce.mappers.GenericMapper;
import com.hust.Ecommerce.util.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface MessageMapper extends GenericMapper<Message, MessageRequest, MessageResponse> {

    @Override
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "roomId", target = "room")
    Message requestToEntity(MessageRequest request);
}
