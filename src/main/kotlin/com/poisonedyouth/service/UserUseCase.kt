package com.poisonedyouth.service

import com.poisonedyouth.model.Identity
import com.poisonedyouth.model.User
import com.poisonedyouth.port.UserPort
import com.poisonedyouth.port.UserRepository

class UserUseCase(
    private val userRepository: UserRepository
) : UserPort {
    override fun addNewUser(user: User): User {
        val existingUser = userRepository.findBy(user.id)
        require(existingUser == null) {
            "User with id ${user.id.getIdOrNull()} already exists."
        }
        return userRepository.save(user)
    }

    override fun updateUser(user: User): User {
        val existingUser = userRepository.findBy(user.id)
        require(existingUser != null) {
            "User with id ${user.id.getIdOrNull()} does not exists."
        }
        return userRepository.update(user)
    }

    override fun deleteUser(userId: Identity) {
        val existingUser = userRepository.findBy(userId)
        require(existingUser != null) {
            "User with id ${userId.getIdOrNull()} does not exists."
        }
        userRepository.delete(existingUser.id)
    }

    override fun findUser(userId: Identity): User {
        val existingUser = userRepository.findBy(userId)
        require(existingUser != null) {
            "User with id ${userId.getIdOrNull()} does not exists."
        }
        return existingUser
    }
}