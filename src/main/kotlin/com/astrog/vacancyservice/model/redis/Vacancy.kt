package com.astrog.vacancyservice.model.redis

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

private const val mouthInSeconds = 30L * 24L * 60L * 60L

@RedisHash("vacancies.actual", timeToLive = mouthInSeconds)
data class Vacancy(
    @Id
    var id: String? = null,
    val remoteId: String,
)