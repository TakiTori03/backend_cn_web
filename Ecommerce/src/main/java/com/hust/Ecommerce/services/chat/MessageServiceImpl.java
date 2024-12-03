package com.hust.Ecommerce.services.chat;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.Ecommerce.constants.ResourceName;
import com.hust.Ecommerce.constants.SearchFields;
import com.hust.Ecommerce.dtos.ListResponse;
import com.hust.Ecommerce.dtos.chat.MessageRequest;
import com.hust.Ecommerce.dtos.chat.MessageResponse;
import com.hust.Ecommerce.entities.chat.Message;
import com.hust.Ecommerce.mappers.chat.MessageMapper;
import com.hust.Ecommerce.repositories.authentication.UserRepository;
import com.hust.Ecommerce.repositories.chat.MessageRepository;
import com.hust.Ecommerce.repositories.chat.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    @Override
    public ListResponse<MessageResponse> findAll(int page, int size, String sort, String filter, String search,
            boolean all) {
        return defaultFindAll(page, size, sort, filter, search, all, SearchFields.MESSAGE, messageRepository,
                messageMapper);
    }

    @Override
    public MessageResponse findById(Long id) {
        return defaultFindById(id, messageRepository, messageMapper, ResourceName.MESSAGE);
    }

    @Override
    public MessageResponse save(MessageRequest request) {
        Message message = messageMapper.requestToEntity(request);

        userRepository.findById(request.getUserId()).ifPresent(message::setUser);

        // (1) Save message
        Message messageAfterSave = messageRepository.save(message);

        // (2) Save room
        roomRepository.findById(request.getRoomId())
                .ifPresent(room -> {
                    room.setUpdatedAt(Instant.now());
                    room.setLastMessage(messageAfterSave);
                    roomRepository.save(room);
                });

        return messageMapper.entityToResponse(messageAfterSave);
    }

    @Override
    public MessageResponse save(Long id, MessageRequest request) {
        return defaultSave(id, request, messageRepository, messageMapper, ResourceName.MESSAGE);
    }

    @Override
    public void delete(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public void delete(List<Long> ids) {
        messageRepository.deleteAllById(ids);
    }

}
