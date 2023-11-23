package com.bilgeadam.rabbitmq.producer;

import com.bilgeadam.rabbitmq.model.RegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterProducer {

    @Value("${rabbitmq.exchange-auth}")
    private String directExchange;

    @Value("${rabbitmq.register-key}")
    private String registerBindingKey;

    //    @Value("${rabbitmq.queue-register}")
//    @Value("register-queue")
//    private String queueNameRegister;

    private final RabbitTemplate rabbitTemplate;

    public void sendNewUser(RegisterModel model){
        System.out.println(registerBindingKey+directExchange);
        rabbitTemplate.convertAndSend(directExchange,registerBindingKey,model);
    }
}
