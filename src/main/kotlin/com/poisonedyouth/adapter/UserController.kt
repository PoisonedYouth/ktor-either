package com.poisonedyouth.adapter

import com.poisonedyouth.model.Address
import com.poisonedyouth.model.UUIDIdentity
import com.poisonedyouth.model.User
import com.poisonedyouth.port.UserPort
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
        return try {
            val user = call.receive<UserDto>().toUser()
            call.respond(HttpStatusCode.Created, userPort.addNewUser(user).toUserDto())
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid input.", e)
            call.respond(HttpStatusCode.BadRequest, "Invalid input: ${e.message}")
        } catch (e: Exception) {
            logger.error("Failed to add new user.", e)
            call.respond(HttpStatusCode.InternalServerError, "Failed to add new user: ${e.message}")
        }
    }

    suspend fun updateUser(call: ApplicationCall) {
        return try {
            val user = call.receive<UserDto>().toUser()
            call.respond(HttpStatusCode.OK, userPort.updateUser(user).toUserDto())
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid input.", e)
            call.respond(HttpStatusCode.BadRequest, "Invalid input: ${e.message}")
        } catch (e: Exception) {
            logger.error("Failed to update user.", e)
            call.respond(HttpStatusCode.InternalServerError, "Failed to update user: ${e.message}")
        }
    }

    suspend fun deleteUser(call: ApplicationCall) {
        return try {
            val userId = call.parameters["userId"] ?: throw IllegalArgumentException("Missing 'userId' parameter.")
            call.respond(HttpStatusCode.Accepted, userPort.deleteUser(UUIDIdentity.fromString(userId)))
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid input.", e)
            call.respond(HttpStatusCode.BadRequest, "Invalid input: ${e.message}")
        } catch (e: Exception) {
            logger.error("Failed to delete user.", e)
            call.respond(HttpStatusCode.BadRequest, "Failed to update user: ${e.message}")
        }
    }

    suspend fun findUser(call: ApplicationCall) {
        return try {
            val userId = call.parameters["userId"] ?: throw IllegalArgumentException("Missing 'userId' parameter.")
            call.respond(HttpStatusCode.Accepted, userPort.findUser(UUIDIdentity.fromString(userId)).toUserDto())
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid input.", e)
            call.respond(HttpStatusCode.BadRequest, "Invalid input: ${e.message}")
        } catch (e: Exception) {
            logger.error("Failed to find user.", e)
            call.respond(HttpStatusCode.BadRequest, "Failed to find user: ${e.message}")
        }
    }
}

private fun UserDto.toUser() = User(
    id = UUIDIdentity.fromStringOrNew(this.id),
    firstName = this.firstName,
    lastName = this.lastName,
    birthDate = this.birthDate,
    address = this.address.toAddress()
)

private fun AddressDto.toAddress() = Address(
    id = UUIDIdentity.fromStringOrNew(this.id),
    streetName = this.streetName,
    streetNumber = this.streetNumber,
    zipCode = this.zipCode,
    city = this.city
)

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

