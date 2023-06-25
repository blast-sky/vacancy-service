package com.astrog.vacancyservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class VacancyServiceApplication

fun main(args: Array<String>) {
	runApplication<VacancyServiceApplication>(*args)
}
