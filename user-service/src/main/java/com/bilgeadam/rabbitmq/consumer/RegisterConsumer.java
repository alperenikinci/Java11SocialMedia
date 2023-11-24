package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.mapper.UserMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j //Console'a log ciktisi vermek icin kullanilan bir kutuphane
public class RegisterConsumer {

    private final UserProfileService userProfileService;

    @RabbitListener(queues = "${rabbitmq.queue-register}") //register-queue
    public void createNewUser(RegisterModel model){
        log.info("User {}",model.toString());
        userProfileService.createUserWithRabbitMq(model);
//        userProfileService.createUserWithRabbitMq(UserMapper.INSTANCE.fromRegisterModelToUserCreateDto(model));
    }



}
