package com.poisonedyouth.port

import com.poisonedyouth.model.Identity
import com.poisonedyouth.model.User

interface UserRepository {
    fun save(user: User): User
    fun findBy(userId: Identity): User?
    fun update(user: User): User
    fun delete(userId: Identity)
}