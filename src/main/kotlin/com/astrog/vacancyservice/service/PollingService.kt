package com.astrog.vacancyservice.service

import com.astrog.vacancyservice.model.dto.VacanciesResponse
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI


@Service
class PollingService(
    private val pollingRestTemplate: RestTemplate,
) {

    fun pool(pages: Int = 20) {
        for(page in 0 until pages) {
            val response = poll(page)

            if(response.found <= 0)
                break

            // todo
        }
    }

    private fun poll(page: Int, perPage: Int = 100): VacanciesResponse {
        val uri: URI = UriComponentsBuilder
            .fromUriString(VACANCIES_URL)
            .queryParam(PAGE, page)
            .queryParam(PER_PAGE, perPage)
            .build()
            .toUri()

        return pollingRestTemplate.getForObject<VacanciesResponse>(uri)
    }

    companion object {
        const val VACANCIES_URL = "vacancies"
        const val PAGE = "page"
        const val PER_PAGE = "per_page"
    }
}