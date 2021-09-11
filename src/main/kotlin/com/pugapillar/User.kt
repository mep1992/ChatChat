package com.pugapillar

import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.sendBlocking
import java.util.concurrent.atomic.*
import kotlin.reflect.KSuspendFunction2

class User(
    private val session: DefaultWebSocketServerSession,
    private val broadcast: suspend (String, String) -> Unit,
    private val leave: suspend (User) -> Unit,
) {
    companion object {
        var lastId = AtomicInteger(0)
    }

    val name = "user${lastId.getAndIncrement()}"

    suspend fun send(message: String) {
        session.send(message)
    }

    suspend fun startReceivingMessages() {
        session.incoming.consumeEach {
            frame ->
            run {
                when (frame) {
                    is Frame.Text -> {
                        val receivedText = frame.readText()
                        if(receivedText.equals("bye", ignoreCase = true)) {
                            leave(this)
                            session.close()
                        } else {
                            broadcast(name, receivedText)
                        }
                    }
                    else -> {
                        session.outgoing.send((Frame.Text("Only text is accepted")))
                    }
                }
            }
        }
    }
}
