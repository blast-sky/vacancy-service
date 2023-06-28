package com.astrog.vacancyservice.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class Employer(
    @JsonProperty("accredited_it_employer")
    val accreditedIdEmployer: Boolean?,

    @JsonProperty("alternate_url")
    val alternateURL: String?,

    @JsonProperty("logo_urls")
    val logoUrls: LogoUrls?,

    @JsonProperty("vacancies_url")
    val vacanciesUrl: String?,

    val id: String?,
    val name: String,
    val trusted: Boolean,
    val url: String?,
)