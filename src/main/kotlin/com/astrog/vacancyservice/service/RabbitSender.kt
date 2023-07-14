package com.astrog.vacancyservice.service

import com.astrog.vacancyservice.model.dto.Item
import org.springframework.amqp.rabbit.core.RabbitMessageOperations
import org.springframework.stereotype.Service

@Service
class RabbitSender(
    private val rabbitMessageOperations: RabbitMessageOperations,
) {

    fun sendVacancy(vacancy: Item) {
        rabbitMessageOperations.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, vacancy)
    }

    companion object {
        const val ROUTING_KEY = "hhsva.service.vacancies.new"
        const val EXCHANGE_NAME = "hhsva.service"
    }
}