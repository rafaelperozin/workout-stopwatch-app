package com.example.gymsessionsstopwatch_wearos.stopwatch

import android.annotation.SuppressLint
import com.example.gymsessionsstopwatch_wearos.sessions.WorkoutSession
import com.example.gymsessionsstopwatch_wearos.sessions.WorkoutSessionManager
import java.text.SimpleDateFormat
import java.util.*

class Stopwatch(private val sessionManager: WorkoutSessionManager? = null) {
    private var startTime: Long = 0
    private var running: Boolean = false
    private var currentTime: Long = 0
    private val dateTimeFormat = "yyyy-MM-dd HH:mm:ss"

    fun start() {
        if (!running) {
            sessionManager?.createNewSession()
            startTime = System.currentTimeMillis()
            running = true
        } else {
            reset()
        }
    }

    fun reset() {
        if (running) {
            saveCurrentBlock()
        }
        startTime = System.currentTimeMillis()
        currentTime = 0
        running = true
    }

    fun stop() {
        saveCurrentBlock()
        running = false
        startTime = 0
        currentTime = 0
    }

    private fun saveCurrentBlock() {
        val blockTime = if (running) System.currentTimeMillis() - startTime else currentTime
        val currentDateTime = Date()
        val simpleDateFormat = SimpleDateFormat(dateTimeFormat, Locale.getDefault())
        val dateTime = simpleDateFormat.format(currentDateTime)

        sessionManager?.addSessionBlock(WorkoutSession.SessionActivityType.ACTIVITY, dateTime, blockTime)
    }

    @SuppressLint("DefaultLocale")
    fun getElapsedTime(): String {
        val elapsed = if (running) System.currentTimeMillis() - startTime else currentTime
        val hours = (elapsed / 3600000).toInt()
        val minutes = (elapsed % 3600000 / 60000).toInt()
        val seconds = ((elapsed % 60000) / 1000).toInt()
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun isRunning(): Boolean = running
}
