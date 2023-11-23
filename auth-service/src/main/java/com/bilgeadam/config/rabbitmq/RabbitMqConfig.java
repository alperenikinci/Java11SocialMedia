package com.bilgeadam.config.rabbitmq;



import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {


//    @Value("${rabbitmq.exchange-auth}")
    @Value("auth-exchange")
    private String exchange;

//    @Value("${rabbitmq.register-key}")
    @Value("register-key")
    private String registerBindingKey;

//    @Value("${rabbitmq.queue-register}")
    @Value("register-queue")
    private String queueNameRegister;



    @Bean
    public DirectExchange exchangeAuth(){
        return new DirectExchange(exchange);
    }
    @Bean
    public Queue registerQueue(){
        return new Queue(queueNameRegister);
    }
    @Bean
    public Binding bindingRegister(final Queue registerQueue, final DirectExchange exchangeAuth){
        return BindingBuilder.bind(registerQueue).to(exchangeAuth).with(registerBindingKey);
    }
}
