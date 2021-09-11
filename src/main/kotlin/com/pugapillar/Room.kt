package com.pugapillar

import java.util.*
import kotlin.collections.LinkedHashSet

class Room {
    private val users: MutableSet<User> = Collections.synchronizedSet(LinkedHashSet())

    suspend fun broadcast(username: String, message: String) {
        broadcast("[${username}]: $message")
    }

    private suspend fun broadcast(formattedMessage: String) {
        users.forEach { user ->
            user.send(formattedMessage)
        }
    }

    suspend fun remove(user: User) {
        users.remove(user)
        broadcast("[${user.name}] has left the chat")
    }

    suspend fun addUser(user: User) {
        users.add(user)
        user.send("You are connected! There are ${users.count()} users here.")
    }
}