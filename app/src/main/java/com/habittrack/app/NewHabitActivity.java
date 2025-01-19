package com.habittrack.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewHabitActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "HabitPrefs";
    private static final String KEY_FREQUENCY = "selectedFrequency";
    private static final String KEY_REMINDER = "selectedReminder";
    private static final String KEY_TIME = "selectedTime";

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
        editor.putStringSet(KEY_REMINDER, new HashSet<>()); // Empty set
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
        String savedTime = sharedPreferences.getString(KEY_TIME, "12:00 PM");
        Object reminderValue = sharedPreferences.getAll().get(KEY_REMINDER);
        String savedReminder;
        if (reminderValue instanceof Set) {
            @SuppressWarnings("unchecked")
            Set<String> selectedDaysSet = (Set<String>) reminderValue;

            // List to maintain the proper order
            List<String> daysOfWeek = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

            // Filter and order the selected days
            List<String> selectedDays = new ArrayList<>();
            for (String day : daysOfWeek) {
                if (selectedDaysSet.contains(day)) {
                    selectedDays.add(day);
                }
            }

            // Determine the reminder value
            if (selectedDays.size() == 7) {
                savedReminder = "Daily  " + savedTime;
            } else if (selectedDays.isEmpty()) {
                savedReminder = "None";
            } else {
                savedReminder = String.join(", ", selectedDays);
                savedReminder += "  " + savedTime;
            }
        } else {
            savedReminder = "None";
        }


        TextView tvFrequency = findViewById(R.id.tv_frequency);
        tvFrequency.setText(savedFrequency);

        TextView tvReminder = findViewById(R.id.tv_reminder);
        tvReminder.setText(savedReminder);
        adjustFontSize(tvReminder, savedReminder);
    }

    private void showBottomTray() {
        BottomSheetDialogFragment bottomSheet = new BottomSheetFragment();
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }

    private void adjustFontSize(TextView textView, String text) {
        // Set an initial font size (you can adjust this starting size based on your needs)
        float fontSize = 14f;  // Starting font size

        // Check the text length and adjust font size accordingly
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        float textWidth = paint.measureText(text);

        // Dynamically decrease the font size if the text width exceeds available width
        while (fontSize > 8f) { // Allow font size to go as low as 8f
            if (textWidth <= 140f) {
                break; // If text fits within available space, exit the loop
            }
            fontSize -= 1; // Decrease font size by 1
            paint.setTextSize(fontSize);
            textWidth = paint.measureText(text); // Recalculate text width
        }

        // Set the new font size to the TextView
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
    }

}
