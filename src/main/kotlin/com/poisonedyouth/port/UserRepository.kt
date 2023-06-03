package com.poisonedyouth.port

import arrow.core.Either
import com.poisonedyouth.failure.Failure
import com.poisonedyouth.model.Identity
import com.poisonedyouth.model.User

interface UserRepository {
    fun save(user: User): Either<Failure, User>
    fun findBy(userId: Identity): Either<Failure, User?>
    fun update(user: User): Either<Failure, User>
    fun delete(userId: Identity): Either<Failure, Unit>
}