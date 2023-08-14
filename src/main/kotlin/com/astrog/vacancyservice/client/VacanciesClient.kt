package com.astrog.vacancyservice.client

import com.astrog.vacancyservice.model.dto.VacanciesResponse
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class VacanciesClient(private val hhWebClient: WebClient) {

    suspend fun getVacancies(page: Int, perPage: Int): VacanciesResponse {
        return hhWebClient.get()
            .uri(VACANCIES_URL)
            .attribute(PAGE, page)
            .attribute(PER_PAGE, perPage)
            .retrieve()
            .awaitBody<VacanciesResponse>()
    }

    companion object {
        const val VACANCIES_URL = "vacancies"
        const val PAGE = "page"
        const val PER_PAGE = "per_page"
    }
}