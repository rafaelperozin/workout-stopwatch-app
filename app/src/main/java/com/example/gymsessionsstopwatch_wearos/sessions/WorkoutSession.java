package com.example.gymsessionsstopwatch_wearos.sessions;

import com.example.gymsessionsstopwatch_wearos.local_files.LocalFileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkoutSession {
    private String sessionId;
    private List<SessionBlock> sessionBlocks;

    public enum SessionActivityType {
        ACTIVITY, REST
    }

    public WorkoutSession() {
        this.sessionId = UUID.randomUUID().toString();
        this.sessionBlocks = new ArrayList<SessionBlock>();
    }

    public void addSessionBlock(SessionActivityType type, String startDate, long timedTime) {
        sessionBlocks.add(new SessionBlock(type, startDate, timedTime));
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<SessionBlock> getSessionBlocks() {
        return sessionBlocks;
    }

    public static class SessionBlock {
        private SessionActivityType type;
        private String startDate;
        private long timedTime;

        public SessionBlock(SessionActivityType type, String startDate, long timedTime) {
            this.type = type;
            this.startDate = startDate;
            this.timedTime = timedTime;
        }

        public long getTimedTime() {
            return timedTime;
        }
    }
}
