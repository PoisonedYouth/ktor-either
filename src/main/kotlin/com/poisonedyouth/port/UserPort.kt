package com.poisonedyouth.port

import arrow.core.Either
import com.poisonedyouth.failure.Failure
import com.poisonedyouth.model.Identity
import com.poisonedyouth.model.User
import com.poisonedyouth.service.UserDto

interface UserPort {
    fun addNewUser(user: UserDto): Either<Failure, User>
    fun updateUser(user: UserDto): Either<Failure, User>
    fun deleteUser(userId: Identity): Either<Failure, Unit>
    fun findUser(userId: Identity): Either<Failure, User>
}