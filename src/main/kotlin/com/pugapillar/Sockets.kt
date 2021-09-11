package com.pugapillar

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.time.*
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        val room = Room()
        webSocket("/chat") {
            println("Adding user!")
            val user = User(this, room::broadcast)
            room.addUser(user)
            user.startReceivingMessages()
        }
    }
}
