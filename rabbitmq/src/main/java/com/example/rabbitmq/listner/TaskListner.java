package com.example.rabbitmq.listner;


import com.example.core.dto.ServiceMessageHistoryDto;
import com.example.rabbitmq.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
@RequiredArgsConstructor
public class TaskListner {

    private final MessageService messageService;

    @RabbitListener(queues = "${spring.rabbitmq.queueTask}")
    public void receiveMessage(ServiceMessageHistoryDto message) {
        messageService.saveMessage(message);
    }

}
