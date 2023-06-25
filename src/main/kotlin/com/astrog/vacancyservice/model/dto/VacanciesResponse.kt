package com.astrog.vacancyservice.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class VacanciesResponse(
    @JsonProperty("per_page")
    val perPage: Long,
    val page: Long,
    val pages: Long,

    val found: Long,

    val items: List<Item>,
)