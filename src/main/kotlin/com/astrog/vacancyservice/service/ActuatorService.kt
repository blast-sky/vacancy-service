package com.astrog.vacancyservice.service

import com.astrog.vacancyservice.model.dto.Item
import com.astrog.vacancyservice.model.redis.Vacancy
import com.astrog.vacancyservice.repository.VacancyRepository
import org.springframework.stereotype.Service

@Service
class ActuatorService(
    private val vacancyRepository: VacancyRepository,
) {

    fun isVacancyPresent(vacancy: Item): Boolean {
        return vacancyRepository.existsById(vacancy.id)
    }

    fun saveVacancy(vacancy: Item) {
        vacancyRepository.save(Vacancy(vacancy.id))
    }
}