package com.astrog.vacancyservice.configuration.resttemplate

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
class RestTemplateConfiguration {

    @Bean
    fun hhRestTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }
}