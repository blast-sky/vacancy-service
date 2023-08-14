package com.astrog.vacancyservice.repository

import org.springframework.data.redis.core.*
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Repository
class VacancyIdRepository(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,
    private val reactiveHashOperations: ReactiveHashOperations<String, String, Boolean>,
) {

    suspend fun existsById(vacancyId: String): Boolean {
        return reactiveHashOperations.hasKeyAndAwait(PREFIX, vacancyId)
    }

    @Transactional
    suspend fun save(vacancyId: String): String {
        reactiveHashOperations.putAndAwait(PREFIX, vacancyId, true)
        reactiveRedisTemplate.expireAndAwait(keyWithPrefix(vacancyId), EXPIRE_DURATION_IN_SECONDS)
        return vacancyId
    }

    fun keyWithPrefix(key: String): String {
        return "$PREFIX:$key"
    }

    companion object {
        const val PREFIX = "vacancy:id"
        val EXPIRE_DURATION_IN_SECONDS: Duration = Duration.ofSeconds(30 * 24 * 60 * 60)
    }
}