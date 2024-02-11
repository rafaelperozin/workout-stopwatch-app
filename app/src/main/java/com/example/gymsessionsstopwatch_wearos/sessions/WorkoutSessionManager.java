package com.example.gymsessionsstopwatch_wearos.sessions;

import android.content.Context;

import com.example.gymsessionsstopwatch_wearos.local_files.LocalFileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WorkoutSessionManager {
    private List<WorkoutSession> sessions;
    private static Context appContext;
    private static final String DEFAULT_TIME = "0:00:00";

    public WorkoutSessionManager(Context context) {
        this.appContext = context.getApplicationContext();
        this.sessions = new ArrayList<>();
    }

    public WorkoutSession createNewSession() {
        WorkoutSession newSession = new WorkoutSession();
        sessions.add(newSession);
        updateSessionsFile();
        return newSession;
    }

    // Adds new block in the latest session
    public void addSessionBlock(WorkoutSession.SessionActivityType type, String startDate, long timedTime) {
        WorkoutSession session = sessions.get(sessions.size() - 1);
        if(session != null) {
            session.addSessionBlock(type, startDate, timedTime);
        }
        updateSessionsFile();
    }

    private void updateSessionsFile() {
        LocalFileManager.saveSessionsToFile(appContext, sessions);
    }

    public static List<WorkoutSession> getAllSessions() {
        List<WorkoutSession> loadedSessions = LocalFileManager.loadSessionFromFile(appContext);
        if (loadedSessions == null) {
            loadedSessions = new ArrayList<>();
        }
        return loadedSessions;
    }

    public static WorkoutSession.SessionBlock getLatestBlock() {
        List<WorkoutSession> loadedSessions = getAllSessions();

        if (loadedSessions.isEmpty()) {
            return null;
        }

        WorkoutSession latestSession = loadedSessions.get(loadedSessions.size() - 1);
        List<WorkoutSession.SessionBlock> blocks = latestSession.getSessionBlocks();

        if (blocks.isEmpty()) {
            return null;
        }

        return blocks.get(blocks.size() - 1);
    };

    public static String getLatestStopwatch() {
        WorkoutSession.SessionBlock latestBlock = getLatestBlock();
        if (latestBlock == null) {
            return DEFAULT_TIME;
        }

        long timedTime = latestBlock.getTimedTime();

        int hours = (int) (timedTime / 3600000);
        int minutes = (int) (timedTime % 3600000) / 60000;
        int seconds = (int) ((timedTime % 60000) / 1000);

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }
}
