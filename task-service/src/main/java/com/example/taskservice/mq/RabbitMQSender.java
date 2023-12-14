package com.example.taskservice.mq;

import com.example.core.dto.ServiceMessageHistoryDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate customRabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.routingkey}")
    private String routingKey;
    @Value("${spring.application.name}")
    private String serviceName;

    public void send(String href) {
        ServiceMessageHistoryDto serviceMessageHistoryDto = new ServiceMessageHistoryDto(serviceName, href);
        customRabbitTemplate.convertAndSend(exchange, routingKey, serviceMessageHistoryDto);
    }
}
