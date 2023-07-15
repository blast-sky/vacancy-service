package com.astrog.vacancyservice.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    private val pullerService: PullerService
) {

    @Scheduled(cron = "0 0/30 * * * * ?")
    fun pullVacancies() {
        pullerService.pullLastPages()
    }
}