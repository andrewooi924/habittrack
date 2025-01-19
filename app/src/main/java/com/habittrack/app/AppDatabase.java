package com.habittrack.app;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Habit.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HabitDao habitDao();
}

