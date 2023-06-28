package com.astrog.vacancyservice.controller

import com.astrog.vacancyservice.model.dto.VacanciesResponse
import com.astrog.vacancyservice.model.redis.Vacancy
import com.astrog.vacancyservice.randomInt
import com.astrog.vacancyservice.randomItem
import com.astrog.vacancyservice.randomString
import com.astrog.vacancyservice.repository.VacancyRepository
import com.astrog.vacancyservice.service.ActuatorService
import com.astrog.vacancyservice.service.PullerService
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject


@SpringBootTest(classes = [ControllerTestConfiguration::class, VacancyPullerController::class])
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class VacancyPullerControllerTest(
    private val mockMvc: MockMvc,
    private val hhRestTemplate: RestTemplate,
    private val rabbitTemplate: RabbitTemplate,
    private val vacancyRepository: VacancyRepository,
) {

    @Test
    fun `pull-out MUST send to rabbit WHEN new items received`() {
        val item = randomItem()

        every { hhRestTemplate.getForObject<VacanciesResponse>(any<String>()) }
            .returns(VacanciesResponse(1, 1, 1, 1, listOf(item)))

        every { vacancyRepository.existsByRemoteId(item.id) }
            .returns(false)

        every { vacancyRepository.save(Vacancy(remoteId = item.id)) }
            .returns(Vacancy(remoteId = randomString()))

        mockMvc.post("/puller/pull-out")
            .andDo { print() }
            .andExpect { status { isOk() } }

        verify {
            hhRestTemplate.getForObject<VacanciesResponse>(any<String>())
            vacancyRepository.existsByRemoteId(item.id)
            vacancyRepository.save(Vacancy(remoteId = item.id))
            rabbitTemplate.convertAndSend(ActuatorService.TOPIC, item)
        }
    }

    @Test
    fun `pull-out MUST not send to rabbit WHEN present items received`() {
        val item1 = randomItem()
        val item2 = item1.copy()

        every { hhRestTemplate.getForObject<VacanciesResponse>(any<String>()) }
            .returns(VacanciesResponse(randomInt(), randomInt(), randomInt(), 2, listOf(item1, item2)))

        every { vacancyRepository.existsByRemoteId(item1.id) }
            .returnsMany(false, true)

        every { vacancyRepository.save(any()) }
            .returns(Vacancy(remoteId = randomString()))

        mockMvc.post("/puller/pull-out")
            .andDo { print() }
            .andExpect { status { isOk() } }

        verify {
            hhRestTemplate.getForObject<VacanciesResponse>(any<String>())
            vacancyRepository.save(Vacancy(remoteId = item1.id))
            rabbitTemplate.convertAndSend(ActuatorService.TOPIC, item1)
        }

        verify(exactly = 2) {
            vacancyRepository.existsByRemoteId(item1.id)
        }
    }

    @Test
    fun `pull-out MUST make 2 request to hh WHEN found == per_page`() {
        val response =
            VacanciesResponse(randomInt(), randomInt(), randomInt(), PullerService.defaultPerPage, emptyList())

        every { hhRestTemplate.getForObject<VacanciesResponse>(any<String>()) }
            .returnsMany(
                response,
                response.copy(found = 0),
            )

        mockMvc.post("/puller/pull-out")
            .andDo { print() }
            .andExpect { status { isOk() } }

        verify(exactly = 2) {
            hhRestTemplate.getForObject<VacanciesResponse>(any<String>())
        }
    }
}