package com.example.gymsessionsstopwatch_wearos.stopwatch;

import android.annotation.SuppressLint;

import com.example.gymsessionsstopwatch_wearos.sessions.WorkoutSession;
import com.example.gymsessionsstopwatch_wearos.sessions.WorkoutSessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Stopwatch {
    private long startTime = 0;
    private boolean running = false;
    private long currentTime = 0;
    private WorkoutSessionManager sessionManager;
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public Stopwatch(WorkoutSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Stopwatch() {

    }

    public void start() {
        if (!running) {
            sessionManager.createNewSession();
            startTime = System.currentTimeMillis();
            running = true;
        } else {
            reset();
        };
    }

    public void reset() {
        if (running) {
            saveCurrentBlock();
        }
        startTime = System.currentTimeMillis();
        currentTime = 0;
        running = true;
    }

    public void stop() {
        saveCurrentBlock();
        running = false;
        startTime = 0;
        currentTime = 0;

    }

    private void saveCurrentBlock() {
        long blockTime = running ? System.currentTimeMillis() - startTime : currentTime;

        Date currentDateTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        String dateTime = simpleDateFormat.format(currentDateTime);

        sessionManager.addSessionBlock(WorkoutSession.SessionActivityType.ACTIVITY, dateTime, blockTime);
    }

    @SuppressLint("DefaultLocale")
    public String getElapsedTime() {
        long elapsed = running ? System.currentTimeMillis() - startTime : currentTime;
        int hours = (int) (elapsed / 3600000);
        int minutes = (int) (elapsed % 3600000) / 60000;
        int seconds = (int) ((elapsed % 60000) / 1000);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public boolean isRunning() {
        return running;
    }
}
