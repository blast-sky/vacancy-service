package com.astrog.vacancyservice.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class VacanciesResponse(
    @JsonProperty("per_page")
    val perPage: Int,
    val page: Int,
    val pages: Int,

    val found: Int,

    val items: List<Item>,
)