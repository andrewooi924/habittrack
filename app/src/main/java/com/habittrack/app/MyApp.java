package com.habittrack.app;

import android.app.Application;

import androidx.room.Room;

public class MyApp extends Application {
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "habit_db")
                .allowMainThreadQueries() // Optional: Avoid in production; use background threads.
                .build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}

