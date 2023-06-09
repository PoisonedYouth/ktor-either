package com.poisonedyouth.service

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.poisonedyouth.failure.Failure
import com.poisonedyouth.model.Address
import com.poisonedyouth.model.Identity
import com.poisonedyouth.model.UUIDIdentity
import com.poisonedyouth.model.User
import com.poisonedyouth.port.UserPort
import com.poisonedyouth.port.UserRepository

class UserUseCase(
    private val userRepository: UserRepository
) : UserPort {
    override fun addNewUser(user: UserDto): Either<Failure, User> = either {
        userRepository.save(user.toUser().bind()).bind()
    }

    override fun updateUser(user: UserDto): Either<Failure, User> = either {
        userRepository.update(user.toUser().bind()).bind()
    }

    override fun deleteUser(userId: Identity): Either<Failure, Unit> {
        return userRepository.delete(userId)
    }

    override fun findUser(userId: Identity): Either<Failure, User> {
        return userRepository.findBy(userId).flatMap { existingUser ->
            existingUser?.right()
                ?: Failure.ValidationFailure("User with id ${userId.getIdOrNull()} does not exists.").left()
        }
    }
}

fun UserDto.toUser(): Either<Failure, User> = either {
    val address = this@toUser.address.toAddress().bind()
    return User(
        id = UUIDIdentity.fromNullableString(this@toUser.id),
        firstName = this@toUser.firstName,
        lastName = this@toUser.lastName,
        birthDate = this@toUser.birthDate,
        address = address
    )
}

private fun AddressDto.toAddress() = Address(
    id = UUIDIdentity.fromNullableString(this.id),
    streetName = this.streetName,
    streetNumber = this.streetNumber,
    zipCode = this.zipCode,
    city = this.city
)