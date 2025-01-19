package com.habittrack.app;

import android.os.Bundle;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReminderActivity extends AppCompatActivity {

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
    }
}