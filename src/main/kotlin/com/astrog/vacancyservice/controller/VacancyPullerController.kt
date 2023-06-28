package com.astrog.vacancyservice.controller

import com.astrog.vacancyservice.service.PullerService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("puller")
class VacancyPullerController(
    private val pullerService: PullerService,
) {

    @Operation(summary = "Invoke pulling and actuation for 20 pages from hh")
    @PostMapping("pull-out")
    @ResponseStatus(HttpStatus.OK)
    fun pull() {
        pullerService.pullLastPages()
    }
}