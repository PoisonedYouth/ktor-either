package com.poisonedyouth.model

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class AddressTest {

    @Test
    fun `Address cannot be created with empty streetName`() {
        // given
        val id = UUIDIdentity(UUID.randomUUID())
        val streetName = ""
        val streetNumber = "13b"
        val zipCode = 12345
        val city = "Los Angeles"


        // when + then
        assertThatThrownBy {
            Address(
                id = id,
                streetName = streetName,
                streetNumber = streetNumber,
                zipCode = zipCode,
                city = city
            )
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessage("The streetName must not be empty!")
    }
}