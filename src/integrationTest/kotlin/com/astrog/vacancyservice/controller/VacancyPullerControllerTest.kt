package com.astrog.vacancyservice.controller

import com.astrog.vacancyservice.model.dto.VacanciesResponse
import com.astrog.vacancyservice.model.redis.Vacancy
import com.astrog.vacancyservice.randomInt
import com.astrog.vacancyservice.randomItem
import com.astrog.vacancyservice.repository.VacancyRepository
import com.astrog.vacancyservice.service.PullerService
import com.astrog.vacancyservice.service.RabbitSender
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitMessageOperations
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
    private val vacancyRepository: VacancyRepository,
    private val rabbitMessageOperations: RabbitMessageOperations,
) {

    @Test
    fun `pull-out MUST send to rabbit WHEN new items received`() {
        val item = randomItem()
        val vacancy = Vacancy(item.id)

        every { hhRestTemplate.getForObject<VacanciesResponse>(any<String>()) }
            .returns(VacanciesResponse(1, 1, 1, 1, listOf(item)))

        every { vacancyRepository.existsById(item.id) }
            .returns(false)

        every { vacancyRepository.save(vacancy) }
            .returns(vacancy)

        mockMvc.post("/puller/pull-out")
            .andDo { print() }
            .andExpect { status { isOk() } }

        verify {
            hhRestTemplate.getForObject<VacanciesResponse>(any<String>())
            vacancyRepository.existsById(item.id)
            vacancyRepository.save(vacancy)
            rabbitMessageOperations.convertAndSend(RabbitSender.EXCHANGE_NAME, RabbitSender.ROUTING_KEY, item)
        }
    }

    @Test
    fun `pull-out MUST not send to rabbit WHEN present items received`() {
        val item1 = randomItem()
        val item2 = item1.copy()
        val vacancy1 = Vacancy(item1.id)

        every { hhRestTemplate.getForObject<VacanciesResponse>(any<String>()) }
            .returns(VacanciesResponse(randomInt(), randomInt(), randomInt(), 2, listOf(item1, item2)))

        every { vacancyRepository.existsById(item1.id) }
            .returnsMany(false, true)

        every { vacancyRepository.save(any()) }
            .returns(vacancy1)

        mockMvc.post("/puller/pull-out")
            .andDo { print() }
            .andExpect { status { isOk() } }

        verify {
            hhRestTemplate.getForObject<VacanciesResponse>(any<String>())
            vacancyRepository.save(vacancy1)
            rabbitMessageOperations.convertAndSend(RabbitSender.EXCHANGE_NAME, RabbitSender.ROUTING_KEY, item1)
        }

        verify(exactly = 2) {
            vacancyRepository.existsById(item1.id)
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