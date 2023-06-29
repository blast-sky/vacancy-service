package com.astrog.vacancyservice.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfiguration {

    @Bean
    fun jacksonMessageParser(jsonObjectMapper: ObjectMapper): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter(jsonObjectMapper)
    }
}