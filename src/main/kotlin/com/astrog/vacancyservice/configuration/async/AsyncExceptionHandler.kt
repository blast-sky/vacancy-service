package com.astrog.vacancyservice.configuration.async

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method


private val logger = KotlinLogging.logger {}

class AsyncExceptionHandler : AsyncUncaughtExceptionHandler {

    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
        logger.error(ex.stackTraceToString())
    }
}