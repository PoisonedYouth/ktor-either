package com.poisonedyouth.model

data class Address(
    val id: Identity,
    val streetName: String,
    val streetNumber: String,
    val zipCode: Int,
    val city: String,
) {
    init {
        require(streetName.isNotEmpty()) {
            "The streetName must not be empty!"
        }
        require(streetNumber.isNotEmpty()) {
            "The streetNumber must not be empty!"
        }
        require(zipCode in 10000..99999) {
            "The zipCode must be between 10000 and 99999!"
        }
        require(city.isNotEmpty()) {
            "The city must not be empty."
        }
    }
}
