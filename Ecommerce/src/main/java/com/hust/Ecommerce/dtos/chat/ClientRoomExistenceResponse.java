package com.hust.Ecommerce.dtos.chat;

import java.util.List;

import lombok.Data;

@Data
public class ClientRoomExistenceResponse {
    private boolean roomExistence;
    private RoomResponse roomResponse;
    private List<MessageResponse> roomRecentMessages;
}
