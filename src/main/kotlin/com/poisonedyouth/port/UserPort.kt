package com.poisonedyouth.port

import com.poisonedyouth.model.Identity
import com.poisonedyouth.model.User

interface UserPort {
    fun addNewUser(user: User): User
    fun updateUser(user: User): User
    fun deleteUser(userId: Identity)
    fun findUser(userId: Identity): User
}