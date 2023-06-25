package com.astrog.vacancyservice.configuration

import com.astrog.vacancyservice.configuration.property.HHProperty
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory

@Component
class PollRestTemplateCustomizer(
    private val hhProperty: HHProperty,
) : RestTemplateCustomizer {

    override fun customize(restTemplate: RestTemplate) {
        restTemplate.uriTemplateHandler = DefaultUriBuilderFactory(hhProperty.url)
    }
}