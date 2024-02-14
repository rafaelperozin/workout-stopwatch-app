package com.example.gymsessionsstopwatch_wearos.sessions

import android.content.Context
import com.example.gymsessionsstopwatch_wearos.local_files.LocalFileManager
import java.util.Locale

class WorkoutSessionManager(context: Context) {
    private var sessions: MutableList<WorkoutSession> = mutableListOf()
    private val appContext: Context = context.applicationContext

    init {
        sessions = mutableListOf()
    }

    fun createNewSession(): WorkoutSession {
        val newSession = WorkoutSession()
        sessions.add(newSession)
        updateSessionsFile()
        return newSession
    }

    fun addSessionBlock(type: WorkoutSession.SessionActivityType, startDate: String, timedTime: Long) {
        sessions.lastOrNull()?.let {
            it.addSessionBlock(type, startDate, timedTime)
            updateSessionsFile()
        }
    }

    private fun updateSessionsFile() {
        LocalFileManager.saveSessionsToFile(appContext, sessions)
    }

    companion object {
        private const val DEFAULT_TIME = "0:00:00"

        fun getAllSessions(context: Context): List<WorkoutSession> {
            return LocalFileManager.loadSessionFromFile(context) ?: listOf()
        }

        private fun getLatestBlock(context: Context): WorkoutSession.SessionBlock? {
            val loadedSessions = getAllSessions(context)
            val latestSession = loadedSessions.lastOrNull() ?: return null
            return latestSession.getSessionBlocks().lastOrNull()
        }

        fun getLatestStopwatch(context: Context): String {
            val latestBlock = getLatestBlock(context) ?: return DEFAULT_TIME
            val timedTime = latestBlock.timedTime
            val hours = (timedTime / 3600000).toInt()
            val minutes = ((timedTime % 3600000) / 60000).toInt()
            val seconds = ((timedTime % 60000) / 1000).toInt()
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        }
    }
}
