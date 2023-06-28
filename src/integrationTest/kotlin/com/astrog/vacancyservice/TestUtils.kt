package com.astrog.vacancyservice

import com.astrog.vacancyservice.model.dto.*
import java.util.*

fun randomString(): String {
    return UUID.randomUUID().toString()
}

fun randomInt(): Int {
    return Random.from { 0 }.nextInt()
}

fun randomItem(): Item {
    return Item(
        acceptIncompleteResumes = true,
        alternateURL = randomString(),
        applyAlternateURL = randomString(),
        hasTest = false,
        professionalRoles = emptyList(),
        publishedAt = randomString(),
        responseLetterRequired = false,
        responseURL = randomString(),
        sortPointDistance = 0.0,
        id = randomString(),
        type = Type(id = "0", name = randomString()),
        url = randomString(),
        name = randomString(),
        area = Region(randomString(), randomString(), randomString()),
        salary = Salary(
            currency = randomString(),
            gross = false,
            from = 0,
            to = 0,
        ),
        snippet = Snippet(requirement = null, responsibility = null),
        employer = Employer(
            accreditedIdEmployer = false,
            alternateURL = randomString(),
            logoUrls = LogoUrls(randomString(), randomString(), randomString()),
            vacanciesUrl = null,
            id = null,
            name = randomString(),
            trusted = false,
            url = randomString(),
        ),
        schedule = null,
        experience = Experience(id = "0", name = randomString())
    )
}