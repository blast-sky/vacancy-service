package com.astrog.vacancyservice.configuration.property

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties("hh")
data class HHProperty(
    @field:NotBlank(message = "hh.url must be not blank")
    val url: String,

    @field:NotBlank(message = "hh.token must be not blank")
    val token: String,
)
