package com.example.rabbitmq.service;


import com.example.core.dto.ServiceMessageHistoryDto;
import com.example.rabbitmq.mapper.MessageMapper;
import com.example.rabbitmq.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public void saveMessage(ServiceMessageHistoryDto message) {
        messageRepository.save(messageMapper.mapToEntity(message));
    }
}
