package com.decoo.psa.config;

import com.decoo.psa.constants.IPFSConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig {

    @Bean
    Queue pinFileQueue() {
        return new Queue(IPFSConstants.IPFS_PIN_BY_CID_JOB, true);
    }

    @Bean
    MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}