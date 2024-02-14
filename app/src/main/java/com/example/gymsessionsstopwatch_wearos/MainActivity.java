package com.example.gymsessionsstopwatch_wearos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.gymsessionsstopwatch_wearos.databinding.ActivityMainBinding;
import com.example.gymsessionsstopwatch_wearos.sessions.WorkoutSession;
import com.example.gymsessionsstopwatch_wearos.sessions.WorkoutSessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private ActivityMainBinding binding;
    private TextView stopwatchDisplay;
    private TextView oldStopwatchDisplay;
    private Button startButton, restButton, stopButton;
    private final Handler stopwatchHandler = new Handler(Looper.getMainLooper());
    private long elapsedTime = 0;
    private boolean isStopwatchRunning = false;
    private static final int DELAY_MILLIS = 500;
    private static final String DEFAULT_TIME = "0:00:00";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private WorkoutSessionManager sessionManager;
    private int curHours = 0;
    private int curMinutes = 0;
    private int curSeconds = 0;

    private final Runnable stopwatchRunnable = new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - elapsedTime;
            curSeconds = (int) (millis / 1000);
            curMinutes = curSeconds / 60;
            curSeconds = curSeconds % 60;
            curHours = curMinutes /60;
            curMinutes = curMinutes % 60;

            stopwatchDisplay.setText(String.format("%d:%02d:%02d", curHours, curMinutes, curSeconds));
            stopwatchHandler.postDelayed(this, DELAY_MILLIS);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new WorkoutSessionManager(this);

        stopwatchDisplay = binding.stopwatchDisplay;
        oldStopwatchDisplay = binding.oldStopwatchDisplay;
        startButton = binding.startButton;
        restButton = binding.restButton;
        stopButton = binding.stopButton;

        startButton.setOnClickListener(view -> {
            if (isStopwatchRunning) {
                resetStopwatch();
            }
            startStopwatch();
            setButtonsState(ButtonState.START);
        });

        restButton.setOnClickListener(view -> {
            resetStopwatch();
            startStopwatch();
            setButtonsState(ButtonState.REST);
        });

        stopButton.setOnClickListener(view -> {
            resetStopwatch();
            startNewSession();
            setButtonsState(ButtonState.RESET);
        });
    }

    private void startStopwatch() {
        if (!isStopwatchRunning) {
            sessionManager.createNewSession();
            allowScreenToSleep(SleepOptions.ON);
        } else {
            saveBlockOnSession(WorkoutSession.SessionActivityType.ACTIVITY);
            setOldStopwatch();
        }
        elapsedTime = System.currentTimeMillis() - elapsedTime;
        stopwatchHandler.post(stopwatchRunnable);
        isStopwatchRunning = true;
    }

    private void resetStopwatch() {
        stopwatchHandler.removeCallbacks(stopwatchRunnable);
        stopwatchDisplay.setText(DEFAULT_TIME);
        elapsedTime = 0;
    }

    private void startNewSession() {
        isStopwatchRunning = false;
        clearOldStopwatch();
        allowScreenToSleep(SleepOptions.OFF);
    }

    private long getTimedTime() {
        // Return time in milliseconds.
        return (curHours * 3600000L) + (curMinutes * 60000L) + (curSeconds * 1000L);
    }

    private void setOldStopwatch() {
        String prevTimedTime = WorkoutSessionManager.Companion.getLatestStopwatch(this);
        oldStopwatchDisplay.setText(prevTimedTime);
        oldStopwatchDisplay.setVisibility(View.VISIBLE);
    }

    private void clearOldStopwatch() {
        oldStopwatchDisplay.setText(DEFAULT_TIME);
        oldStopwatchDisplay.setVisibility(View.GONE);
    }

    private enum SleepOptions {
        ON, OFF
    }

    private void allowScreenToSleep(SleepOptions value) {
        if (value == SleepOptions.ON) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }

    private void saveBlockOnSession(WorkoutSession.SessionActivityType type) {
        Date currentDateTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        String dateTime = simpleDateFormat.format(currentDateTime);
        sessionManager.addSessionBlock(type, dateTime, getTimedTime());
    }

    private enum ButtonState {
        START, REST, RESET
    }

    @SuppressLint("SetTextI18n")
    private void setButtonsState(ButtonState state) {
        boolean isStartVisible = state == ButtonState.RESET || state == ButtonState.REST;
        boolean isRestVisible = state == ButtonState.START;
        boolean isStopVisible = state == ButtonState.START || state == ButtonState.REST;

        startButton.setVisibility(isStartVisible ? View.VISIBLE : View.GONE);
        restButton.setVisibility(isRestVisible ? View.VISIBLE : View.GONE);
        stopButton.setVisibility(isStopVisible ? View.VISIBLE : View.GONE);
        startButton.setText(state == ButtonState.REST ? "CONTINUE" : "START");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopwatchHandler.removeCallbacks(stopwatchRunnable);
        binding = null;
    }
}