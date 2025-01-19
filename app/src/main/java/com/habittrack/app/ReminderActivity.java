package com.habittrack.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class ReminderActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "HabitPrefs";
    private static final String KEY_REMINDER = "selectedReminder";
    private static final String KEY_TIME = "selectedTime";
    private SharedPreferences sharedPreferences;
    private LinearLayout layoutTime;
    private TextView tvMon, tvTue, tvWed, tvThu, tvFri, tvSat, tvSun, tvSelect, tvTime;
    private ArrayList<TextView> dayViews;
    private Set<String> selectedDays;
    private String selectedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.status));

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            // Set title
            getSupportActionBar().setTitle("Reminder");
            // Enable back button (close activity button)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle toolbar back button click
        toolbar.setNavigationOnClickListener(v -> {
            // Close the activity when the back button is pressed
            onBackPressed();
        });

        layoutTime = findViewById(R.id.layoutTime);
        tvSelect = findViewById(R.id.tvSelect);
        tvMon = findViewById(R.id.tvMon);
        tvTue = findViewById(R.id.tvTue);
        tvWed = findViewById(R.id.tvWed);
        tvThu = findViewById(R.id.tvThu);
        tvFri = findViewById(R.id.tvFri);
        tvSat = findViewById(R.id.tvSat);
        tvSun = findViewById(R.id.tvSun);
        tvTime = findViewById(R.id.tv_time);

        dayViews = new ArrayList<>(Arrays.asList(tvMon, tvTue, tvWed, tvThu, tvFri, tvSat, tvSun));

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        selectedDays = sharedPreferences.getStringSet(KEY_REMINDER, new HashSet<>());
        selectedTime = sharedPreferences.getString(KEY_TIME, "12:00 PM");

        restoreSelections();

        for (TextView dayView : dayViews) {
            dayView.setOnClickListener(this::toggleDaySelection);
        }

        tvSelect.setOnClickListener(v -> {
            if (selectedDays.size() == 7) {
                // Deselect all
                selectedDays.clear();
                tvSelect.setText("Select all");
            } else {
                // Select all
                selectedDays.addAll(Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
                tvSelect.setText("Deselect all");
            }
            updateSelections();
            saveSelections();

            layoutTime.setVisibility(selectedDays.isEmpty() ? View.GONE : View.VISIBLE);
        });

        tvTime.setOnClickListener(v -> {
            // Get the current time
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Create the MaterialTimePicker
            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(hour)
                    .setMinute(minute)
                    .setTitleText("Select Time")
                    .build();

            picker.addOnPositiveButtonClickListener(view -> {
                int selectedHour = picker.getHour();
                int selectedMinute = picker.getMinute();

                // Set the time to the TextView in 12-hour format
                String time = String.format("%02d:%02d %s",
                        selectedHour % 12 == 0 ? 12 : selectedHour % 12,
                        selectedMinute,
                        selectedHour < 12 ? "AM" : "PM");
                tvTime.setText(time);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_TIME, time);
                editor.apply();
            });

            // Show the picker
            picker.show(getSupportFragmentManager(), picker.toString());
        });
    }

    private void toggleDaySelection(View view) {
        TextView dayView = (TextView) view;
        String day = dayView.getText().toString();

        if (selectedDays.contains(day)) {
            selectedDays.remove(day);
        } else {
            selectedDays.add(day);
        }

        updateSelections();
        saveSelections();

        // Update Select All/Deselect All text
        if (selectedDays.size() == 7) {
            tvSelect.setText("Deselect all");
        } else {
            tvSelect.setText("Select all");
        }

        layoutTime.setVisibility(selectedDays.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void updateSelections() {
        for (TextView dayView : dayViews) {
            if (selectedDays.contains(dayView.getText().toString())) {
                dayView.setSelected(true); // Change background to selected
            } else {
                dayView.setSelected(false); // Change background to unselected
            }
        }
    }

    private void restoreSelections() {
        for (TextView dayView : dayViews) {
            if (selectedDays.contains(dayView.getText().toString())) {
                dayView.setSelected(true);
                layoutTime.setVisibility(View.VISIBLE);
                tvTime.setText(selectedTime);
            } else {
                dayView.setSelected(false);
            }
        }

        // Update Select All/Deselect All text
        if (selectedDays.size() == 7) {
            tvSelect.setText("Deselect all");
        } else {
            tvSelect.setText("Select all");
        }
    }

    private void saveSelections() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_REMINDER, selectedDays);
        editor.apply();
    }
}