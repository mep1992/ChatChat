package com.pugapillar

import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.sendBlocking
import java.util.concurrent.atomic.*
import kotlin.reflect.KSuspendFunction2

class User(
    private val session: DefaultWebSocketSession,
    private val broadcast: KSuspendFunction2<String, String, Unit>
) {
    companion object {
        var lastId = AtomicInteger(0)
    }

    private val name = "user${lastId.getAndIncrement()}"

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
                        broadcast(name, receivedText)
                    }
                    else -> {
                        session.outgoing.send((Frame.Text("Only text is accepted")))
                    }
                }
            }
        }
    }
}
