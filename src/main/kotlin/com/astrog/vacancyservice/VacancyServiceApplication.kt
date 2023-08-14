package com.astrog.vacancyservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
class VacancyServiceApplication

fun main(args: Array<String>) {
    runApplication<VacancyServiceApplication>(*args)
}
