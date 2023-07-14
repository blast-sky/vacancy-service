package com.astrog.vacancyservice.controller

import com.astrog.vacancyservice.model.dto.VacanciesResponse
import com.astrog.vacancyservice.model.redis.Vacancy
import com.astrog.vacancyservice.randomInt
import com.astrog.vacancyservice.randomItem
import com.astrog.vacancyservice.repository.VacancyRepository
import com.astrog.vacancyservice.service.PullerService
import com.astrog.vacancyservice.service.RabbitSender
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.contains
import org.springframework.amqp.rabbit.core.RabbitMessageOperations
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@ActiveProfiles("test")
@SpringBootTest(classes = [ControllerTestConfiguration::class])
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class VacancyPullerControllerTest(
    private val mockMvc: MockMvc,
    private val mockRestServiceServer: MockRestServiceServer,
    private val vacancyRepository: VacancyRepository,
    private val rabbitMessageOperations: RabbitMessageOperations,
    private val objectMapper: ObjectMapper,
) {

    @Test
    fun `pull-out MUST send to rabbit WHEN new items received`() {
        val item = randomItem()
        val vacancy = Vacancy(item.id)

        val responseVacancyJson = objectMapper.writeValueAsString(VacanciesResponse(1, 1, 1, 1, listOf(item)))
        mockRestServiceServer.expect { contains(PullerService.VACANCIES_URL) }
            .andRespond(withSuccess(responseVacancyJson, MediaType.APPLICATION_JSON))

        every { vacancyRepository.existsById(item.id) }
            .returns(false)

        every { vacancyRepository.save(vacancy) }
            .returns(vacancy)

        mockMvc.post("/puller/pull-out")
            .andDo { print() }
            .andExpect { status { isOk() } }

        verify {
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

        val responseVacancyJson = objectMapper.writeValueAsString(VacanciesResponse(1, 1, 1, 1, listOf(item1, item2)))
        mockRestServiceServer.expect { contains(PullerService.VACANCIES_URL) }
            .andRespond(withSuccess(responseVacancyJson, MediaType.APPLICATION_JSON))

        every { vacancyRepository.existsById(item1.id) }
            .returnsMany(false, true)

        every { vacancyRepository.save(any()) }
            .returns(vacancy1)

        mockMvc.post("/puller/pull-out")
            .andDo { print() }
            .andExpect { status { isOk() } }

        verify {
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

        val responseVacancyJson = objectMapper.writeValueAsString(response)
        mockRestServiceServer.expect {
            contains(PullerService.VACANCIES_URL)
            contains("page=1")
        }
            .andRespond(withSuccess(responseVacancyJson, MediaType.APPLICATION_JSON))

        val responseVacancyJson2 = objectMapper.writeValueAsString(response.copy(found = 0))
        mockRestServiceServer.expect {
            contains(PullerService.VACANCIES_URL)
            contains("page=2")
        }
            .andRespond(withSuccess(responseVacancyJson2, MediaType.APPLICATION_JSON))

        mockMvc.post("/puller/pull-out")
            .andDo { print() }
            .andExpect { status { isOk() } }
    }
}