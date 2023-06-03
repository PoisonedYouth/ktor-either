package com.poisonedyouth.service

import arrow.core.left
import arrow.core.right
import com.poisonedyouth.adapter.InMemoryUserRepository
import com.poisonedyouth.failure.Failure
import com.poisonedyouth.model.UUIDIdentity
import com.poisonedyouth.port.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.util.UUID

class UserUseCaseTest {

    private val userRepository: UserRepository = spy(InMemoryUserRepository())
    private val userUseCase = UserUseCase(userRepository)

    @Test
    fun `addNewUser throws exception if user already exists`() {
        // given
        val id = UUIDIdentity(UUID.randomUUID())
        doReturn(
            UserDto(
                id = id.id.toString(),
                firstName = "John",
                lastName = "Doe",
                birthDate = LocalDate.of(2000, 1, 1),
                address = AddressDto(
                    id = id.id.toString(),
                    streetName = "Main Street",
                    streetNumber = "122",
                    zipCode = 22222,
                    city = "Los Angeles"
                )
            ).toUser()
        ).whenever(userRepository).findBy(any())

        val user = UserDto(
            id = id.id.toString(),
            firstName = "Joe",
            lastName = "Black",
            birthDate = LocalDate.of(1999, 1, 1),
            address = AddressDto(
                id = id.id.toString(),
                streetName = "Main Street",
                streetNumber = "122",
                zipCode = 22222,
                city = "Los Angeles"
            )
        )

        // when
        val actual = userUseCase.addNewUser(user)

        // then
        assertThat(actual.leftOrNull()?.message)
            .isEqualTo("User with id '${id.id}' already exists.")
    }

    @Test
    fun `addNewUser throws exception if loading of user fails`() {
        // given
        val id = UUIDIdentity(UUID.randomUUID())
        whenever(userRepository.findBy(id)).thenAnswer {
            Failure.ValidationFailure("Failed!").left()
        }

        val user = UserDto(
            id = id.id.toString(),
            firstName = "Joe",
            lastName = "Black",
            birthDate = LocalDate.of(1999, 1, 1),
            address = AddressDto(
                id = UUID.randomUUID().toString(),
                streetName = "Main Street",
                streetNumber = "122",
                zipCode = 22222,
                city = "Los Angeles"
            )
        )

        // when
        val actual = userUseCase.addNewUser(user)

        // then
        assertThat(actual.leftOrNull()?.message).isEqualTo("Failed!")
    }
}