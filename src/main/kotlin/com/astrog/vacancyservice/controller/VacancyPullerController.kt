package com.astrog.vacancyservice.controller

import com.astrog.vacancyservice.service.PullerService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger { }

@RestController
@RequestMapping("puller")
class VacancyPullerController(
    private val pullerService: PullerService,
) {

    val pullerMutex = Mutex()

    val pullerScope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.IO +
                CoroutineExceptionHandler { _, throwable ->
                    logger.error(throwable.stackTraceToString())
                }
    )

    @Operation(summary = "Invoke pulling and actuation for 20 pages from hh")
    @PostMapping("pull-out")
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun pull() {
        pullerScope.launch {
            pullerMutex.withLock { pullerService.pullLatestPages() }
        }
    }
}