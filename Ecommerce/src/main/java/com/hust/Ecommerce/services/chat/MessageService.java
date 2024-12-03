package com.hust.Ecommerce.services.chat;

import com.hust.Ecommerce.dtos.chat.MessageRequest;
import com.hust.Ecommerce.dtos.chat.MessageResponse;
import com.hust.Ecommerce.services.CrudService;

public interface MessageService extends CrudService<Long, MessageRequest, MessageResponse> {

}
