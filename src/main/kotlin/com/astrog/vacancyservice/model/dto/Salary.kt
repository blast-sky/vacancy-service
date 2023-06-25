package com.astrog.vacancyservice.model.dto

data class Salary(
    val currency: String,
    val gross: Boolean,
    val from: Int,
    val to: Int,
)