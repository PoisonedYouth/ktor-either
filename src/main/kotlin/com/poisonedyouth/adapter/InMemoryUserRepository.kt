package com.poisonedyouth.adapter

import com.poisonedyouth.model.Identity
import com.poisonedyouth.model.User
import com.poisonedyouth.port.UserRepository

class InMemoryUserRepository : UserRepository {
    private val userList = mutableListOf<User>()

    override fun save(user: User): User {
        val successfullyAdded = userList.add(user)
        if (!successfullyAdded) {
            error("User could not be added.")
        }
        return user
    }

    override fun findBy(userId: Identity): User? {
        return userList.find { it.id == userId }
    }

    override fun update(user: User): User {
        val existingUser = findBy(user.id) ?: error("User does not exist.")
        userList.remove(existingUser)
        val updatedUser = existingUser.copy(
            firstName = user.firstName,
            lastName = user.lastName,
            birthDate = user.birthDate,
            address = user.address
        )
        userList.add(
            updatedUser
        )
        return updatedUser
    }

    override fun delete(userId: Identity) {
        userList.removeIf { it.id == userId }
    }
}