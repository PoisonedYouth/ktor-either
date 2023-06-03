package com.poisonedyouth.failure

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.slf4j.Logger

sealed interface Failure {
    val message: String

    data class ValidationFailure(override val message: String) : Failure

    data class GenericFailure(val e: Exception) : Failure {
        override val message: String = e.localizedMessage
    }
}

fun <T> eval(logger: Logger, exec: () -> T): Either<Failure, T> {
    return try {
        exec().right()
    } catch (e: Exception) {
        logger.error("Failed to execute operation because of - ${e.message}")
        Failure.GenericFailure(e).left()
    }
}
