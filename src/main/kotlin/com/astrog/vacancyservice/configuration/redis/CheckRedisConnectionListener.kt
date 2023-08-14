package com.astrog.vacancyservice.configuration.redis

import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Profile("!test")
@Component
class CheckRedisConnectionListener(
    private val redisTemplate: RedisTemplate<String, String>,
) {

//    @EventListener
//    fun handleContextRefreshEvent(refresh: ContextRefreshedEvent) {
//        redisTemplate.opsForSet().isMember("", "") // dummy call
//    }
}