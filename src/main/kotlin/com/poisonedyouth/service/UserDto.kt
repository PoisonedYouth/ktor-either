package com.poisonedyouth.service

import java.time.LocalDate

data class UserDto(
    val id: String?,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val address: AddressDto,
)
