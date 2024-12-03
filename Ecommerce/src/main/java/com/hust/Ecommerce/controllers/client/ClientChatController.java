package com.hust.Ecommerce.controllers.client;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hust.Ecommerce.constants.MessageKeys;
import com.hust.Ecommerce.dtos.ApiResponse;
import com.hust.Ecommerce.dtos.chat.ClientRoomExistenceResponse;
import com.hust.Ecommerce.dtos.chat.RoomResponse;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.entities.chat.Message;
import com.hust.Ecommerce.entities.chat.Room;
import com.hust.Ecommerce.exceptions.payload.ResourceNotFoundException;
import com.hust.Ecommerce.mappers.chat.MessageMapper;
import com.hust.Ecommerce.mappers.chat.RoomMapper;
import com.hust.Ecommerce.repositories.chat.MessageRepository;
import com.hust.Ecommerce.repositories.chat.RoomRepository;
import com.hust.Ecommerce.services.authentication.AuthenticationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/client-api/chat")
@AllArgsConstructor
public class ClientChatController {

        private RoomRepository roomRepository;
        private RoomMapper roomMapper;
        private MessageRepository messageRepository;
        private MessageMapper messageMapper;
        private AuthenticationService authenticationService;

        @GetMapping("/get-room")
        public ResponseEntity<ApiResponse<?>> getRoom() {
                User user = authenticationService.getUserWithAuthorities()
                                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.USER_NOT_FOUND));
                RoomResponse roomResponse = roomRepository.findByUser(user)
                                .map(roomMapper::entityToResponse)
                                .orElse(null);

                var clientRoomExistenceResponse = new ClientRoomExistenceResponse();
                clientRoomExistenceResponse.setRoomExistence(roomResponse != null);
                clientRoomExistenceResponse.setRoomResponse(roomResponse);
                clientRoomExistenceResponse.setRoomRecentMessages(
                                roomResponse != null
                                                ? messageMapper.entityToResponse(
                                                                messageRepository
                                                                                .findByRoomId(
                                                                                                roomResponse.getId(),
                                                                                                PageRequest.of(0, 20,
                                                                                                                Sort.by(Sort.Direction.DESC,
                                                                                                                                "id")))
                                                                                .stream()
                                                                                .sorted(Comparator.comparing(
                                                                                                Message::getId))
                                                                                .collect(Collectors.toList()))
                                                : Collections.emptyList());

                return ResponseEntity.ok(ApiResponse.<ClientRoomExistenceResponse>builder().success(true)
                                .payload(clientRoomExistenceResponse).build());
        }

        @PostMapping("/create-room")
        public ResponseEntity<ApiResponse<?>> createRoom() {
                User user = authenticationService.getUserWithAuthorities()
                                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.USER_NOT_FOUND));

                if (user.getRoom() != null)
                        throw new RuntimeException(MessageKeys.ROOM_EXISTED);

                Room room = new Room();
                room.setName(user.getName());
                room.setUser(user);

                Room roomAfterSave = roomRepository.save(room);

                RoomResponse roomResponse = roomMapper.entityToResponse(roomAfterSave);
                return ResponseEntity
                                .ok(ApiResponse.<RoomResponse>builder().success(true).payload(roomResponse).build());
        }
}
