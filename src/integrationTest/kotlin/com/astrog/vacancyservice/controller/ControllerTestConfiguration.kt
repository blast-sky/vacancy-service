package com.astrog.vacancyservice.controller

import com.astrog.vacancyservice.repository.VacancyRepository
import com.ninjasquad.springmockk.MockkBean
import org.springframework.amqp.rabbit.core.RabbitMessageOperations
import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration
class ControllerTestConfiguration {

    @MockkBean(relaxed = true)
    lateinit var vacancyRepository: VacancyRepository

    @MockkBean(relaxed = true)
    lateinit var rabbitMessageOperations: RabbitMessageOperations
}