package com.poisonedyouth.adapter

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.poisonedyouth.failure.Failure
import com.poisonedyouth.failure.eval
import com.poisonedyouth.model.Identity
import com.poisonedyouth.model.NoIdentity
import com.poisonedyouth.model.UUIDIdentity
import com.poisonedyouth.model.User
import com.poisonedyouth.port.UserRepository
import org.slf4j.LoggerFactory
import java.util.UUID

open class InMemoryUserRepository : UserRepository {
    private val logger = LoggerFactory.getLogger(InMemoryUserRepository::class.java)

    private val userList = mutableListOf<User>()

    private fun addToList(user: User): Either<Failure, User> {
        val id = getOrCreateId(user)
        eval(logger) {
            userList.add(user)
        }
        return User(
            id = id,
            firstName = user.firstName,
            lastName = user.lastName,
            birthDate = user.birthDate,
            address = user.address
        )
    }

    override fun save(user: User): Either<Failure, User> = either {
        val existingUser = findBy(user.id).bind()
        ensure(existingUser == null) {
            Failure.ValidationFailure("User with id '${user.id.getIdOrNull()}' already exists.")
        }
        addToList(user).bind()
    }

    override fun findBy(userId: Identity): Either<Failure, User?> {
        return eval(logger) {
            userList.find { it.id == userId }
        }
    }

    override fun update(user: User): Either<Failure, User> {
        return findBy(user.id).flatMap { existingUser ->
            if (existingUser == null) {
                Failure.ValidationFailure("User with id '${user.id}' does not exists!").left()
            }
            eval(logger) {
                userList.remove(existingUser)
                addToList(user)
                user
            }
        }
    }

    override fun delete(userId: Identity) = eval(logger) {
        userList.removeIf { it.id == userId }
        Unit
    }

    private fun getOrCreateId(user: User) = if (user.id is NoIdentity) {
        UUIDIdentity(UUID.randomUUID())
    } else {
        user.id
    }
}