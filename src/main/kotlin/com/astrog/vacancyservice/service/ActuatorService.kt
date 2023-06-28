package com.astrog.vacancyservice.service

import com.astrog.vacancyservice.model.dto.Item
import com.astrog.vacancyservice.model.redis.Vacancy
import com.astrog.vacancyservice.repository.VacancyRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class ActuatorService(
    private val vacancyRepository: VacancyRepository,
    private val rabbitTemplate: RabbitTemplate,
) {

    fun checkVacanciesAndNotifyAboutNew(vacancies: List<Item>) {
        for(vacancy in vacancies) {
            val isPresent = vacancyRepository.existsByRemoteId(vacancy.id)
            if(!isPresent) {
                vacancyRepository.save(Vacancy(remoteId = vacancy.id))
                rabbitTemplate.convertAndSend(TOPIC, vacancy)
            }
        }
    }

    companion object {
        const val TOPIC = "vacancies.new"
    }
}