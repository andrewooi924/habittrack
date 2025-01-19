package com.habittrack.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NewHabitActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "HabitPrefs";
    private static final String KEY_FREQUENCY = "selectedFrequency";
    private static final String KEY_REMINDER = "selectedReminder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.status));

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            // Set title
            getSupportActionBar().setTitle("New Habit");
            // Enable back button (close activity button)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle toolbar back button click
        toolbar.setNavigationOnClickListener(v -> {
            // Close the activity when the back button is pressed
            onBackPressed();
        });

        EditText habitName = findViewById(R.id.et_habit_name);
        EditText habitDescription = findViewById(R.id.et_habit_description);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FREQUENCY, "None");
        editor.putString(KEY_REMINDER, "None");
        editor.apply();

        // Set up the Frequency TextView
        TextView tvFrequency = findViewById(R.id.tv_frequency);
        tvFrequency.setOnClickListener(v -> {
            Intent intent = new Intent(NewHabitActivity.this, FrequencyActivity.class);
            startActivity(intent);
        });

        // Set up the Reminder TextView
        TextView tvReminder = findViewById(R.id.tv_reminder);
        tvReminder.setOnClickListener(v -> {
            Intent intent = new Intent(NewHabitActivity.this, ReminderActivity.class);
            startActivity(intent);
        });


        // Set up the Categories TextView to show the BottomSheet
        TextView tvCategories = findViewById(R.id.tv_categories);
        tvCategories.setOnClickListener(v -> {
            showBottomTray();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedFrequency = sharedPreferences.getString(KEY_FREQUENCY, "None");
        String savedReminder = sharedPreferences.getString(KEY_REMINDER, "None");

        TextView tvFrequency = findViewById(R.id.tv_frequency);
        tvFrequency.setText(savedFrequency);

        TextView tvReminder = findViewById(R.id.tv_reminder);
        tvReminder.setText(savedReminder);
    }

    private void showBottomTray() {
        BottomSheetDialogFragment bottomSheet = new BottomSheetFragment();
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }
}
