package com.astrog.vacancyservice.controller

import com.astrog.vacancyservice.repository.VacancyRepository
import com.astrog.vacancyservice.service.ActuatorService
import com.astrog.vacancyservice.service.PullerService
import com.astrog.vacancyservice.service.RabbitSender
import com.ninjasquad.springmockk.MockkBean
import org.springframework.amqp.rabbit.core.RabbitMessageOperations
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
    lateinit var rabbitMessageOperations: RabbitMessageOperations

    @Bean
    fun actuatorService(vacancyRepository: VacancyRepository): ActuatorService {
        return ActuatorService(vacancyRepository)
    }

    @Bean
    fun rabbitSender(rabbitMessageOperations: RabbitMessageOperations): RabbitSender {
        return RabbitSender(rabbitMessageOperations)
    }

    @Bean
    fun pullerService(hhRestTemplate: RestTemplate, actuatorService: ActuatorService, rabbitSender: RabbitSender): PullerService {
        return PullerService(hhRestTemplate, actuatorService, rabbitSender)
    }
}