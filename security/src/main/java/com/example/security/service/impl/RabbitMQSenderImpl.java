package com.example.security.service.impl;


import com.example.core.dto.ServiceMessageHistoryDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("security")
public class RabbitMQSenderImpl {

    @Autowired
    private AmqpTemplate customRabbitTemplate;

    @Value("${spring.rabbitmq.exchangeSecurity}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkeySecurity}")
    private String routingKey;
    @Value("${spring.application.name}")
    private String serviceName;
    public void send(String href) {
        ServiceMessageHistoryDto serviceMessageHistoryDto = new ServiceMessageHistoryDto(serviceName, href);
        customRabbitTemplate.convertAndSend(exchange, routingKey, serviceMessageHistoryDto);
    }
}