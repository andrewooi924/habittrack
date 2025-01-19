package com.habittrack.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FrequencyActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "HabitPrefs";
    private static final String KEY_FREQUENCY = "selectedFrequency";
    private LinearLayout radioGroupFrequency;
    private LinearLayout layoutWeeklyMonthly;
    private TextView tvFrequencyValue;
    private ImageButton btnMinus, btnPlus;
    private int currentFrequencyValue = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.status));

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            // Set title
            getSupportActionBar().setTitle("Frequency");
            // Enable back button (close activity button)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle toolbar back button click
        toolbar.setNavigationOnClickListener(v -> {
            // Close the activity when the back button is pressed
            onBackPressed();
        });

        radioGroupFrequency = findViewById(R.id.radioGroupFrequency);
        layoutWeeklyMonthly = findViewById(R.id.layoutWeeklyMonthly);
        tvFrequencyValue = findViewById(R.id.tvFrequencyValue);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedFrequency = sharedPreferences.getString(KEY_FREQUENCY, "None");

        clearAllTickIcons();
        setInitialSelection(savedFrequency);

        setupCustomRadioGroup();

        btnMinus.setOnClickListener(v -> updateFrequencyValue(false));
        btnPlus.setOnClickListener(v -> updateFrequencyValue(true));
        updateButtonStates();
    }

    private void setupCustomRadioGroup() {
        // Iterate through each child view in the LinearLayout
        for (int i = 0; i < radioGroupFrequency.getChildCount(); i++) {
            View child = radioGroupFrequency.getChildAt(i);
            if (child instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) child;

                // Set click listener for each RadioButton
                radioButton.setOnClickListener(v -> {
                    clearAllTickIcons();
                    showTickIcon(radioButton);

                    String selectedFrequency = radioButton.getText().toString();
                    if (selectedFrequency.equals("None") || selectedFrequency.equals("Daily")) {
                        layoutWeeklyMonthly.setVisibility(View.GONE);
                        saveFrequencySelection(selectedFrequency);
                    }
                    else {
                        layoutWeeklyMonthly.setVisibility(View.VISIBLE);

                        if (selectedFrequency.equals("Weekly")) {
                            currentFrequencyValue = 1;
                            updateButtonStates();
                            tvFrequencyValue.setText(currentFrequencyValue + " / Week");
                            selectedFrequency = currentFrequencyValue + " / Week";
                        } else if (selectedFrequency.equals("Monthly")) {
                            currentFrequencyValue = 1;
                            updateButtonStates();
                            tvFrequencyValue.setText(currentFrequencyValue + " / Month");
                            selectedFrequency = currentFrequencyValue + " / Month";
                        }

                        saveFrequencySelection(selectedFrequency);
                    }
                });
            }
        }
    }

    // Helper method to show the tick icon
    private void showTickIcon(RadioButton radioButton) {
        radioButton.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                ContextCompat.getDrawable(this, R.drawable.ic_check),
                null
        );
    }

    private void clearAllTickIcons() {
        for (int i = 0; i < radioGroupFrequency.getChildCount(); i++) {
            View child = radioGroupFrequency.getChildAt(i);
            if (child instanceof RadioButton) {
                ((RadioButton) child).setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }
    }

    private void setInitialSelection(String frequency) {
        for (int i = 0; i < radioGroupFrequency.getChildCount(); i++) {
            View child = radioGroupFrequency.getChildAt(i);
            if (child instanceof RadioButton) {
                if (((RadioButton) child).getText().toString().equalsIgnoreCase(frequency)) {
                    showTickIcon((RadioButton) child);
                    break;
                }
            }
        }
    }

    private void saveFrequencySelection(String frequency) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FREQUENCY, frequency);
        editor.apply();
    }

    private void updateFrequencyValue(boolean increment) {
        if (increment) {
            if (layoutWeeklyMonthly.getVisibility() == View.VISIBLE && tvFrequencyValue.getText().toString().contains("Week")) {
                if (currentFrequencyValue < 7) {
                    currentFrequencyValue++;
                    tvFrequencyValue.setText(currentFrequencyValue + " / Week");
                }
            } else if (layoutWeeklyMonthly.getVisibility() == View.VISIBLE && tvFrequencyValue.getText().toString().contains("Month")) {
                if (currentFrequencyValue < 31) {
                    currentFrequencyValue++;
                    tvFrequencyValue.setText(currentFrequencyValue + " / Month");
                }
            }
        } else {
            if (layoutWeeklyMonthly.getVisibility() == View.VISIBLE && tvFrequencyValue.getText().toString().contains("Week")) {
                if (currentFrequencyValue > 1) {
                    currentFrequencyValue--;
                    tvFrequencyValue.setText(currentFrequencyValue + " / Week");
                }
            } else if (layoutWeeklyMonthly.getVisibility() == View.VISIBLE && tvFrequencyValue.getText().toString().contains("Month")) {
                if (currentFrequencyValue > 1) {
                    currentFrequencyValue--;
                    tvFrequencyValue.setText(currentFrequencyValue + " / Month");
                }
            }
        }

        updateButtonStates();
        saveFrequencySelection(tvFrequencyValue.getText().toString().contains("Week") ? currentFrequencyValue + " / Week" : currentFrequencyValue + " / Month");
    }

    private void updateButtonStates() {
        // Disable buttons if they reach their limits
        if (currentFrequencyValue <= 1) {
            btnMinus.setEnabled(false);
            btnMinus.setImageResource(R.drawable.ic_minus_lmt);
        } else {
            btnMinus.setEnabled(true);
        }

        if (currentFrequencyValue >= (tvFrequencyValue.getText().toString().contains("Week") ? 7 : 31)) {
            btnPlus.setEnabled(false);
            btnPlus.setImageResource(R.drawable.ic_plus_lmt);
        } else {
            btnPlus.setEnabled(true);
        }
    }
}