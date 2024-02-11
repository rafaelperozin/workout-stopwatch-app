package com.example.gymsessionsstopwatch_wearos.local_files;

import android.content.Context;
import com.google.gson.Gson;

import com.example.gymsessionsstopwatch_wearos.sessions.WorkoutSession;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class LocalFileManager {
    private static final String FILE_NAME = "workout_sessions.json";

    public static void saveSessionsToFile(Context context, List<WorkoutSession> sessions) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(sessions);

        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<WorkoutSession> loadSessionFromFile(Context context) {
        FileInputStream fis = null;
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (!file.exists()) {
                return new ArrayList<>();
            }

            fis = context.openFileInput(FILE_NAME);
            InputStreamReader reader = new InputStreamReader(fis);
            Gson gson = new Gson();

            return gson.fromJson(reader, new TypeToken<List<WorkoutSession>>() {}.getType());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
