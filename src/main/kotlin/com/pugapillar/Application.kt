package com.pugapillar

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import java.time.Duration

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
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
                val user = User(this, room::broadcast, room::remove)
                room.addUser(user)
                user.startReceivingMessages()
            }
        }
    }.start(wait = true)
}
