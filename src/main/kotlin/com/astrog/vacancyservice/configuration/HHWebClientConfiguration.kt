package com.astrog.vacancyservice.configuration

import com.astrog.vacancyservice.configuration.property.HHProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class HHWebClientConfiguration(
    private val hhProperty: HHProperty,
) {

    @Bean
    fun hhWebClient(): WebClient {
        return WebClient.create(hhProperty.baseUrl)
    }
}