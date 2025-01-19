package com.habittrack.app;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private LinearLayout btnHome, btnStats;
    private View currentSelectedTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHome = findViewById(R.id.btnHome);
        btnStats = findViewById(R.id.btnStats);

        // Default tab on opening app
        setTabSelected(btnHome);
        loadFragment(new HomeFragment());

        btnHome.setOnClickListener(v -> {
            setTabSelected(btnHome);
            loadFragment(new HomeFragment());
        });

        btnStats.setOnClickListener(v -> {
            setTabSelected(btnStats);
            loadFragment(new StatsFragment());
        });

        // Access the database
        AppDatabase db = MyApp.getDatabase();

        // Example: Insert a habit
//        Habit habit = new Habit();
//        habit.setTitle("Exercise");
//        habit.setDescription("Workout for 30 minutes");
//        habit.setColor("#FF5733");
//        habit.setIcon(R.drawable.ic_exercise);
//        habit.setCategory("Health");

//        new Thread(() -> db.habitDao().insertHabit(habit)).start(); // Perform in a background thread.
    }

    private void setTabSelected(View selectedTab) {
        if (currentSelectedTab != null) {
            currentSelectedTab.setSelected(false);
        }

        selectedTab.setSelected(true);
        currentSelectedTab = selectedTab;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();

        setNavigationBarColor();
    }

    private void setNavigationBarColor() {
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.status));
    }
}
