package com.astrog.vacancyservice.configuration.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.ReactiveHashOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate

@Configuration
class ReactiveRedisConfiguration {

    @Bean
    fun reactiveHashOperation(
        redisTemplate: ReactiveRedisTemplate<String, String>
    ): ReactiveHashOperations<String, String, Boolean> {
        return redisTemplate.opsForHash()
    }
}