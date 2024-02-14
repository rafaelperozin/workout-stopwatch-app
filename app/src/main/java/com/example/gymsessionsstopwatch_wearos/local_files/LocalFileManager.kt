package com.example.gymsessionsstopwatch_wearos.local_files

import android.content.Context
import com.example.gymsessionsstopwatch_wearos.sessions.WorkoutSession
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

object LocalFileManager {
    private const val FILE_NAME = "workout_sessions.json"

    fun saveSessionsToFile(context: Context, sessions: List<WorkoutSession>) {
        val gson = Gson()
        val jsonString = gson.toJson(sessions)
        try {
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { fos ->
                OutputStreamWriter(fos).use { writer ->
                    writer.write(jsonString)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadSessionsFromFile(context: Context): List<WorkoutSession>? {
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (!file.exists()) {
                return arrayListOf()
            }
            context.openFileInput(FILE_NAME).use { fis ->
                InputStreamReader(fis).use { reader ->
                    val gson = Gson()
                    return gson.fromJson(reader, object : TypeToken<List<WorkoutSession>>() {}.type)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
