package com.astrog.vacancyservice.client

import com.astrog.vacancyservice.model.dto.VacanciesResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "vacancies-client", url = "\${hh.base-url}")
interface VacanciesClient {

    @GetMapping(VACANCIES_URL)
    fun getVacancies(
        @RequestParam(PAGE) page: Int,
        @RequestParam(PER_PAGE) perPage: Int
    ): VacanciesResponse

    companion object {
        const val VACANCIES_URL = "vacancies"
        const val PAGE = "page"
        const val PER_PAGE = "per_page"
    }
}