package com.astrog.vacancyservice.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {

    @Bean
    fun apiDocConfig(buildProperties: BuildProperties): OpenAPI{
        return OpenAPI().apply {
            info = Info().apply {
                title = "Vacancy Service"
                description = "Vacancy pulling and actuating"
                version = buildProperties.version
            }
        }
    }
}