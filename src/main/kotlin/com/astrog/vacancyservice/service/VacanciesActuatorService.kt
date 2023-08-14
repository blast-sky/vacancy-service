package com.astrog.vacancyservice.service

import com.astrog.vacancyservice.model.dto.Item
import com.astrog.vacancyservice.repository.VacancyIdRepository
import org.springframework.stereotype.Service

@Service
class VacanciesActuatorService(
    private val vacancyIdRepository: VacancyIdRepository,
) {

    suspend fun isVacancyPresent(vacancy: Item): Boolean {
        return vacancyIdRepository.existsById(vacancy.id)
    }

    suspend fun saveVacancy(vacancy: Item) {
        vacancyIdRepository.save(vacancy.id)
    }
}