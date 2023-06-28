package com.astrog.vacancyservice.service

import com.astrog.vacancyservice.model.dto.VacanciesResponse
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.util.UriComponentsBuilder


@Service
class PullerService(
    private val hhRestTemplate: RestTemplate,
    private val actuatorService: ActuatorService,
) {

    fun pullLastPages(pages: Int = defaultPages, perPage: Int = defaultPerPage) {
        for(page in 0 until pages) {
            val response = pull(page, perPage)

            actuatorService.checkVacanciesAndNotifyAboutNew(response.items)

            if(response.found < perPage)
                break
        }
    }

    private fun pull(page: Int, perPage: Int): VacanciesResponse {
        val uri = UriComponentsBuilder
            .fromUriString(VACANCIES_URL)
            .queryParam(PAGE, page)
            .queryParam(PER_PAGE, perPage)
            .build()
            .toUriString()

        return hhRestTemplate.getForObject<VacanciesResponse>(uri)
    }

    companion object {
        const val VACANCIES_URL = "vacancies"
        const val PAGE = "page"
        const val PER_PAGE = "per_page"

        const val defaultPages = 20
        const val defaultPerPage = 100
    }
}