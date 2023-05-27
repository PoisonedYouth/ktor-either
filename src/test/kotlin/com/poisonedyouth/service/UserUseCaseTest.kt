package com.poisonedyouth.service

import com.poisonedyouth.model.Address
import com.poisonedyouth.model.UUIDIdentity
import com.poisonedyouth.model.User
import com.poisonedyouth.port.UserRepository
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.util.UUID

class UserUseCaseTest {

    private val userRepository: UserRepository = mock()
    private val userUseCase = UserUseCase(userRepository)

    @Test
    fun `addNewUser throws exception if user already exists`() {
        // given
        val id = UUIDIdentity(UUID.randomUUID())
        whenever(userRepository.findBy(id)).thenReturn(
            User(
                id = id,
                firstName = "John",
                lastName = "Doe",
                birthDate = LocalDate.of(2000, 1, 1),
                address = Address(
                    id = UUIDIdentity(UUID.randomUUID()),
                    streetName = "Main Street",
                    streetNumber = "122",
                    zipCode = 22222,
                    city = "Los Angeles"
                )
            )
        )
        val user = User(
            id = id,
            firstName = "Joe",
            lastName = "Black",
            birthDate = LocalDate.of(1999, 1, 1),
            address = Address(
                id = UUIDIdentity(UUID.randomUUID()),
                streetName = "Main Street",
                streetNumber = "122",
                zipCode = 22222,
                city = "Los Angeles"
            )
        )

        // when + then
        assertThatThrownBy {
            userUseCase.addNewUser(user)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("User with id ${id.id} already exists.")
    }

    @Test
    fun `addNewUser throws exception if loading of user fails`() {
        // given
        val id = UUIDIdentity(UUID.randomUUID())
        whenever(userRepository.findBy(id)).thenThrow(IllegalStateException("Failed!"))
        val user = User(
            id = id,
            firstName = "Joe",
            lastName = "Black",
            birthDate = LocalDate.of(1999, 1, 1),
            address = Address(
                id = UUIDIdentity(UUID.randomUUID()),
                streetName = "Main Street",
                streetNumber = "122",
                zipCode = 22222,
                city = "Los Angeles"
            )
        )

        // when + then
        assertThatThrownBy {
            userUseCase.addNewUser(user)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Failed!")
    }
}