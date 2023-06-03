package com.poisonedyouth.model

import org.assertj.core.api.Assertions.assertThat
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

        // when
        val actual = Address(
            id = id,
            streetName = streetName,
            streetNumber = streetNumber,
            zipCode = zipCode,
            city = city
        )

        // then
        assertThat(actual.leftOrNull()?.message).isEqualTo("The streetName must not be empty!")
    }

    @Test
    fun `Address cannot be created with empty streetNumber`() {
        // given
        val id = UUIDIdentity(UUID.randomUUID())
        val streetName = "Main Street"
        val streetNumber = ""
        val zipCode = 12345
        val city = "Los Angeles"

        // when
        val actual = Address(
            id = id,
            streetName = streetName,
            streetNumber = streetNumber,
            zipCode = zipCode,
            city = city
        )

        // then
        assertThat(actual.leftOrNull()?.message).isEqualTo("The streetNumber must not be empty!")
    }
}