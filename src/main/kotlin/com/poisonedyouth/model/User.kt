package com.poisonedyouth.model

import java.time.LocalDate
import java.util.UUID

data class User(
    val id: Identity,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val address: Address,
) {
    init {
        require(firstName.isNotEmpty()) {
            "Firstname must not be empty!"
        }
        require(lastName.isNotEmpty()) {
            "Lastname must not be empty!"
        }
        require(birthDate in LocalDate.of(1900, 1, 1)..LocalDate.now()) {
            "Birthdate must be between 1900 and ${LocalDate.now().year}"
        }
    }
}

data class UserId(
    val id: UUID
)

sealed interface Identity {
    fun getIdOrNull(): UUID? {
        return when (this) {
            is UUIDIdentity -> this.id
            NoIdentity -> null
        }
    }

    fun getIdOrThrow(): UUID {
        return when (this) {
            is UUIDIdentity -> this.id
            NoIdentity -> error("No id available.")
        }
    }
}

object NoIdentity : Identity

data class UUIDIdentity(val id: UUID) : Identity {
    companion object {
        fun fromStringOrNew(value: String?): Identity {
            if (value == null) {
                return UUIDIdentity(UUID.randomUUID())
            }
            return UUIDIdentity(UUID.fromString(value))
        }

        fun fromString(value: String): Identity {
            return UUIDIdentity(UUID.fromString(value))
        }
    }
}
