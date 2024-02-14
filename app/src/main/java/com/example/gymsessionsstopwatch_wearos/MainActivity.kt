package com.example.gymsessionsstopwatch_wearos

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import com.example.gymsessionsstopwatch_wearos.databinding.ActivityMainBinding
import com.example.gymsessionsstopwatch_wearos.sessions.WorkoutSession
import com.example.gymsessionsstopwatch_wearos.sessions.WorkoutSessionManager
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {
    private lateinit var binding: ActivityMainBinding
    private val stopwatchHandler = Handler(Looper.getMainLooper())
    private var elapsedTime = 0L
    private var isStopwatchRunning = false
    private val delayMillis = 500L
    private val defaultTime = "0:00:00"
    private val dateTimeFormat = "yyyy-MM-dd HH:mm:ss"
    private lateinit var sessionManager: WorkoutSessionManager
    private lateinit var currentRunnable: Runnable
    private var curHours = 0
    private var curMinutes = 0
    private var curSeconds = 0

    private fun stopwatchRunnable(): Runnable = Runnable {
        currentRunnable = stopwatchRunnable()
        val millis = System.currentTimeMillis() - elapsedTime
        curSeconds = (millis / 1000).toInt()
        curMinutes = curSeconds / 60
        curSeconds %= 60
        curHours = curMinutes / 60
        curMinutes %= 60

        binding.stopwatchDisplay.text = String.format("%d:%02d:%02d", curHours, curMinutes, curSeconds)
        stopwatchHandler.postDelayed(currentRunnable, delayMillis)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = WorkoutSessionManager(this)

        binding.startButton.setOnClickListener {
            if (isStopwatchRunning) {
                resetStopwatch()
            }
            startStopwatch()
            setButtonsState(ButtonState.START)
        }

        binding.restButton.setOnClickListener {
            resetStopwatch()
            startStopwatch()
            setButtonsState(ButtonState.REST)
        }

        binding.stopButton.setOnClickListener {
            resetStopwatch()
            startNewSession()
            setButtonsState(ButtonState.RESET)
        }
    }

    private fun startStopwatch() {
        if (!isStopwatchRunning) {
            sessionManager.createNewSession()
            allowScreenToSleep(false)
        } else {
            saveBlockOnSession(WorkoutSession.SessionActivityType.ACTIVITY)
            setOldStopwatch()
        }
        elapsedTime = System.currentTimeMillis() - elapsedTime
        stopwatchHandler.post(stopwatchRunnable())
        isStopwatchRunning = true
    }

    private fun resetStopwatch() {
        stopwatchHandler.removeCallbacks(currentRunnable)
        binding.stopwatchDisplay.text = defaultTime
        elapsedTime = 0L
    }

    private fun startNewSession() {
        isStopwatchRunning = false
        clearOldStopwatch()
        allowScreenToSleep(true)
    }

    private fun getTimedTime() = (curHours * 3600000L) + (curMinutes * 60000L) + (curSeconds * 1000L)

    private fun setOldStopwatch() {
        val prevTimedTime = WorkoutSessionManager.getLatestStopwatch(this)
        binding.oldStopwatchDisplay.apply {
            text = prevTimedTime
            visibility = View.VISIBLE
        }
    }

    private fun clearOldStopwatch() {
        binding.oldStopwatchDisplay.apply {
            text = defaultTime
            visibility = View.INVISIBLE
        }
    }

    private fun allowScreenToSleep(allow: Boolean) {
        if (allow) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    private fun saveBlockOnSession(type: WorkoutSession.SessionActivityType) {
        val currentDateTime = Date()
        val simpleDateFormat = SimpleDateFormat(dateTimeFormat, Locale.getDefault())
        val dateTime = simpleDateFormat.format(currentDateTime)
        sessionManager.addSessionBlock(type, dateTime, getTimedTime())
    }

    @SuppressLint("SetTextI18n")
    private fun setButtonsState(state: ButtonState) {
        with(binding) {
            startButton.visibility = if (state == ButtonState.RESET || state == ButtonState.REST) View.VISIBLE else View.GONE
            restButton.visibility = if (state == ButtonState.START) View.VISIBLE else View.GONE
            stopButton.visibility = if (state == ButtonState.START || state == ButtonState.REST) View.VISIBLE else View.INVISIBLE
            startButton.text = if (state == ButtonState.REST) "CONTINUE" else "START"
        }
    }

    enum class ButtonState { START, REST, RESET }

    override fun onDestroy() {
        super.onDestroy()
        stopwatchHandler.removeCallbacks(currentRunnable)
        startNewSession()
        setButtonsState(ButtonState.RESET)
    }
}
