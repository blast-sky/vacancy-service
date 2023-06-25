package com.astrog.vacancyservice.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class Item(
    @JsonProperty("accept_incomplete_resumes")
    val acceptIncompleteResumes: Boolean,

    @JsonProperty("alternate_url")
    val alternateURL: String,

    @JsonProperty("apply_alternate_url")
    val applyAlternateURL: String,

    @JsonProperty("has_test")
    val hasTest: Boolean,

    @JsonProperty("professional_roles")
    val professionalRoles: List<Any?>,

    @JsonProperty("published_at")
    val publishedAt: String,

    @JsonProperty("response_letter_required")
    val responseLetterRequired: Boolean,

    @JsonProperty("response_url")
    val responseURL: String? = null,

    @JsonProperty("sort_point_distance")
    val sortPointDistance: Double,

   // val address: Address?,

    val id: String,
    val type: Type,
    val url: String,
    val name: String,
    val area: Region,
    val salary: Salary,
    val snippet: Snippet,
    val employer: Employer,
    val schedule: Schedule?,
    val experience: Experience,
)