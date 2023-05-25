package com.poisonedyouth.plugins

import com.poisonedyouth.adapter.UserController
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userController: UserController by inject()

    routing {
        route("api/v1/user") {
            post {
                userController.addNewUser(call)
            }
            patch {
                userController.updateUser(call)
            }
            delete("{userId}") {
                userController.deleteUser(call)
            }
            get("{userId}") {
                userController.findUser(call)
            }
        }
    }
}
