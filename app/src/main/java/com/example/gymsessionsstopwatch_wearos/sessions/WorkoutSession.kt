package com.example.gymsessionsstopwatch_wearos.sessions

import java.util.UUID

class WorkoutSession {
    val sessionId: String = UUID.randomUUID().toString()
    private val sessionBlocks: MutableList<SessionBlock> = mutableListOf()

    fun addSessionBlock(type: SessionActivityType, startDate: String, timedTime: Long) {
        sessionBlocks.add(SessionBlock(type, startDate, timedTime))
    }

    fun getSessionBlocks(): List<SessionBlock> = sessionBlocks

    enum class SessionActivityType {
        ACTIVITY, REST
    }

    class SessionBlock(val type: SessionActivityType, val startDate: String, val timedTime: Long)
}