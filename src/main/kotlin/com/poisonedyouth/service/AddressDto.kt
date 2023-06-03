package com.poisonedyouth.service

data class AddressDto(
    val id: String?,
    val streetName: String,
    val streetNumber: String,
    val zipCode: Int,
    val city: String,
)