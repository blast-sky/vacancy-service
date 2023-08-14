package com.astrog.vacancyservice.controller

import com.astrog.vacancyservice.client.VacanciesClient
import com.astrog.vacancyservice.model.dto.VacanciesResponse
import com.astrog.vacancyservice.randomInt
import com.astrog.vacancyservice.randomItem
import com.astrog.vacancyservice.repository.VacancyIdRepository
import com.astrog.vacancyservice.service.PullerService
import com.astrog.vacancyservice.service.RabbitSender
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitMessageOperations
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient

@ActiveProfiles("test")
@SpringBootTest(properties = ["hh.base-url=localhost"])
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class VacancyPullerControllerTest(
    private val webTestClient: WebTestClient,
) {

    @MockkBean
    lateinit var vacanciesClient: VacanciesClient

    @MockkBean(relaxed = true)
    lateinit var vacancyIdRepository: VacancyIdRepository

    @MockkBean(relaxed = true)
    lateinit var rabbitMessageOperations: RabbitMessageOperations

    @Test
    fun `pull-out MUST send to rabbit WHEN new items received`() {
        val item = randomItem()
        val vacancyId = item.id

        coEvery { vacanciesClient.getVacancies(any(), any()) }
            .returns(VacanciesResponse(1, 1, 1, 1, listOf(item)))

        coEvery { vacancyIdRepository.existsById(item.id) }
            .returns(false)

        coEvery { vacancyIdRepository.save(vacancyId) }
            .returns(vacancyId)

        webTestClient.post()
            .uri("/puller/pull-out")
            .exchange()
            .expectStatus().isAccepted

        coVerify {
            vacanciesClient.getVacancies(any(), any())
            vacancyIdRepository.existsById(item.id)
            vacancyIdRepository.save(vacancyId)
            rabbitMessageOperations.convertAndSend(RabbitSender.EXCHANGE_NAME, RabbitSender.ROUTING_KEY, item)
        }
    }

    @Test
    fun `pull-out MUST not send to rabbit WHEN present items received`() {
        val item1 = randomItem()
        val item2 = item1.copy()
        val vacancy = item1.id

        coEvery { vacanciesClient.getVacancies(any(), any()) }
            .returns(VacanciesResponse(1, 1, 1, 1, listOf(item1, item2)))

        coEvery { vacancyIdRepository.existsById(item1.id) }
            .returnsMany(false, true)

        coEvery { vacancyIdRepository.save(any()) }
            .returns(vacancy)

        webTestClient.post()
            .uri("/puller/pull-out")
            .exchange()
            .expectStatus().isAccepted

        coVerify {
            vacancyIdRepository.save(vacancy)
            rabbitMessageOperations.convertAndSend(RabbitSender.EXCHANGE_NAME, RabbitSender.ROUTING_KEY, item1)
        }

        coVerify(exactly = 2) {
            vacancyIdRepository.existsById(item1.id)
        }
    }

    @Test
    fun `pull-out MUST make 2 request to hh WHEN first request have found == per_page`() {
        val response =
            VacanciesResponse(randomInt(), randomInt(), randomInt(), PullerService.defaultPerPage, emptyList())

        coEvery { vacanciesClient.getVacancies(any(), any()) }
            .returnsMany(
                response,
                response.copy(found = 0)
            )

        webTestClient.post()
            .uri("/puller/pull-out")
            .exchange()
            .expectStatus().isAccepted
    }
}