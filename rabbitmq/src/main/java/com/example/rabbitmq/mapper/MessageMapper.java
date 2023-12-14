package com.example.rabbitmq.mapper;

import com.example.core.dto.ServiceMessageHistoryDto;
import com.example.rabbitmq.entity.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageMapper {
    public Message mapToEntity(ServiceMessageHistoryDto message) {
        Message entity = new Message();
        entity.setTimeResponse(LocalDateTime.now());
        entity.setServiceName(message.nameService());
        entity.setUrlMethod(message.urlMethod());
        return entity;
    }
}
