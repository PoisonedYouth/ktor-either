package com.poisonedyouth.adapter

import com.poisonedyouth.failure.Failure
import com.poisonedyouth.model.Address
import com.poisonedyouth.model.UUIDIdentity
import com.poisonedyouth.model.User
import com.poisonedyouth.port.UserPort
import com.poisonedyouth.service.AddressDto
import com.poisonedyouth.service.UserDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.slf4j.LoggerFactory

class UserController(
    private val userPort: UserPort
) {
    private val logger = LoggerFactory.getLogger(UserController::class.java)

    suspend fun addNewUser(call: ApplicationCall) {
        val user = call.receive<UserDto>()
        userPort.addNewUser(user).fold({ failure -> handleFailure(failure, call) }) {
            call.respond(HttpStatusCode.Created, it.toUserDto())
        }
    }

    suspend fun updateUser(call: ApplicationCall) {
        val user = call.receive<UserDto>()
        userPort.updateUser(user).fold({ failure -> handleFailure(failure, call) }) {
            call.respond(HttpStatusCode.OK, it.toUserDto())
        }
    }

    suspend fun deleteUser(call: ApplicationCall) {
        val userId = call.parameters["userId"]
        val identity = UUIDIdentity.fromNullableString(userId)
        userPort.deleteUser(identity).fold({ failure -> handleFailure(failure, call) }) {
            call.respond(HttpStatusCode.Accepted, Unit)
        }
    }

    suspend fun findUser(call: ApplicationCall) {
        val userId = call.parameters["userId"]
        val identity = UUIDIdentity.fromNullableString(userId)
        userPort.findUser(identity).fold({ failure -> handleFailure(failure, call) }) {
            call.respond(HttpStatusCode.Accepted, it)
        }
    }

    private suspend fun handleFailure(failure: Failure, call: ApplicationCall) {
        when (failure) {
            is Failure.ValidationFailure -> {
                logger.error("Invalid input: ${failure.message}.")
                call.respond(HttpStatusCode.BadRequest, "Invalid input: ${failure.message}")
            }

            is Failure.GenericFailure -> {
                logger.error("Failed to complete operation.", failure.e)
                call.respond(HttpStatusCode.InternalServerError, "Failed to complete operation: ${failure.message}")
            }
        }
    }
}

private fun User.toUserDto() = UserDto(
    id = this.id.getIdOrNull().toString(),
    firstName = this.firstName,
    lastName = this.lastName,
    birthDate = this.birthDate,
    address = this.address.toAddressDto()
)

private fun Address.toAddressDto() = AddressDto(
    id = this.id.getIdOrNull().toString(),
    streetName = this.streetName,
    streetNumber = this.streetNumber,
    zipCode = this.zipCode,
    city = this.city
)

