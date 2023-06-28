package com.astrog.vacancyservice.repository

import com.astrog.vacancyservice.model.redis.Vacancy
import org.springframework.data.repository.CrudRepository

interface VacancyRepository : CrudRepository<Vacancy, String> {

    fun existsByRemoteId(remoteId: String): Boolean
}