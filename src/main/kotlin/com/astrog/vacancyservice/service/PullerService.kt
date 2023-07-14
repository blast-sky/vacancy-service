package com.astrog.vacancyservice.service

import com.astrog.vacancyservice.client.VacanciesClient
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service


@Service
class PullerService(
    private val vacanciesClient: VacanciesClient,
    private val actuatorService: ActuatorService,
    private val rabbitSender: RabbitSender,
) {

    @Async
    fun pullLastPages(pages: Int = defaultPages, perPage: Int = defaultPerPage) {
        for(page in 0 until pages) {
            val pullResponse = vacanciesClient.getVacancies(page, perPage)

            val newVacancies = pullResponse.items
                .filterNot(actuatorService::isVacancyPresent)

            newVacancies.forEach { newVacancy ->
                actuatorService.saveVacancy(newVacancy)
                rabbitSender.sendVacancy(newVacancy)
            }

            if(pullResponse.found < perPage)
                break
        }
    }

    companion object {
        const val defaultPages = 20
        const val defaultPerPage = 100
    }
}