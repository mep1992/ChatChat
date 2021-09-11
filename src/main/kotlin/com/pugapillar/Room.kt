package com.pugapillar

import java.util.*
import kotlin.collections.LinkedHashSet

class Room {
    private val users: MutableSet<User> = Collections.synchronizedSet(LinkedHashSet())

    suspend fun broadcast(username: String, message: String) {
        users.forEach {
            user -> user.send("[${username}]: $message")
        }
    }

    suspend fun addUser(user: User) {
        users.add(user)
        user.send("You are connected! There are ${users.count()} users here.")
    }
}