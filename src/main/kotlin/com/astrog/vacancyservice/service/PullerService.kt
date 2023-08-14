package com.astrog.vacancyservice.service

import com.astrog.vacancyservice.client.VacanciesClient
import org.springframework.stereotype.Service


@Service
class PullerService(
    private val vacanciesClient: VacanciesClient,
    private val vacanciesActuatorService: VacanciesActuatorService,
    private val rabbitSender: RabbitSender,
) {

    suspend fun pullLatestPages(pages: Int = defaultPages, perPage: Int = defaultPerPage) {
        for (page in 0 until pages) {
            val pullResponse = vacanciesClient.getVacancies(page, perPage)

            val newVacancies = pullResponse.items
                .filterNot { vacanciesActuatorService.isVacancyPresent(it) }

            newVacancies.forEach { newVacancy ->
                vacanciesActuatorService.saveVacancy(newVacancy)
                rabbitSender.sendVacancy(newVacancy)
            }

            if (pullResponse.found < perPage)
                break
        }
    }

    companion object {
        const val defaultPages = 20
        const val defaultPerPage = 100
    }
}