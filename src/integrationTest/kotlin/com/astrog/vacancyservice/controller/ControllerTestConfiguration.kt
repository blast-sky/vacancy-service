package com.astrog.vacancyservice.controller

import com.astrog.vacancyservice.repository.VacancyRepository
import com.astrog.vacancyservice.service.ActuatorService
import com.astrog.vacancyservice.service.PullerService
import com.ninjasquad.springmockk.MockkBean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ControllerTestConfiguration {

    @MockkBean
    lateinit var hhRestTemplate: RestTemplate

    @MockkBean(relaxed = true)
    lateinit var vacancyRepository: VacancyRepository

    @MockkBean(relaxed = true)
    lateinit var rabbitTemplate: RabbitTemplate

    @Bean
    fun actuatorService(vacancyRepository: VacancyRepository, rabbitTemplate: RabbitTemplate): ActuatorService {
        return ActuatorService(vacancyRepository, rabbitTemplate)
    }

    @Bean
    fun pullerService(hhRestTemplate: RestTemplate, actuatorService: ActuatorService): PullerService {
        return PullerService(hhRestTemplate, actuatorService)
    }
}