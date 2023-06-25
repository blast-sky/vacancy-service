package com.astrog.vacancyservice.controller

import com.astrog.vacancyservice.service.PollingService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("poller")
class VacancyPollerController(
    private val pollingService: PollingService,
) {

    @PostMapping("execute")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun startPolling() {
        pollingService.pool()
    }
}