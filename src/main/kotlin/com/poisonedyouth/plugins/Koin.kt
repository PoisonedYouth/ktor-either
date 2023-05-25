package com.poisonedyouth.plugins

import com.poisonedyouth.adapter.InMemoryUserRepository
import com.poisonedyouth.adapter.UserController
import com.poisonedyouth.service.UserUseCase
import com.poisonedyouth.port.UserPort
import com.poisonedyouth.port.UserRepository
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.KoinApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun KoinApplication.defaultModule() = modules(defaultModule)
val defaultModule = module {
    singleOf(::UserUseCase) bind UserPort::class
    singleOf(::UserController) bind UserController::class
    singleOf(::InMemoryUserRepository) bind UserRepository::class
}

fun Application.configureDependencyInjection() {
    // Install Ktor features
    install(Koin) {
        slf4jLogger()
        modules(defaultModule)
    }

}