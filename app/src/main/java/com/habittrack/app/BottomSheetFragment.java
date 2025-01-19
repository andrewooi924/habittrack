package com.habittrack.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.habittrack.app.R;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private CheckBox checkboxOption1;
    private CheckBox checkboxOption2;
    private CheckBox checkboxOption3;
    private Button btnSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the bottom sheet layout
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        // Bind views
        checkboxOption1 = view.findViewById(R.id.checkbox_option_1);
        checkboxOption2 = view.findViewById(R.id.checkbox_option_2);
        checkboxOption3 = view.findViewById(R.id.checkbox_option_3);
        btnSave = view.findViewById(R.id.btn_save);

        // Set click listener for the save button
        btnSave.setOnClickListener(v -> {
            // Handle save action (get selected options)
            StringBuilder selectedOptions = new StringBuilder("Selected Options: ");
            if (checkboxOption1.isChecked()) {
                selectedOptions.append("Option 1, ");
            }
            if (checkboxOption2.isChecked()) {
                selectedOptions.append("Option 2, ");
            }
            if (checkboxOption3.isChecked()) {
                selectedOptions.append("Option 3, ");
            }
            // Display selected options or do further processing
            String selectedText = selectedOptions.toString();
            // Remove trailing comma if exists
            if (selectedText.endsWith(", ")) {
                selectedText = selectedText.substring(0, selectedText.length() - 2);
            }

            // You can return or display the result here
            // For example, show a Toast
            // Toast.makeText(getContext(), selectedText, Toast.LENGTH_SHORT).show();

            // Dismiss the bottom sheet
            dismiss();
        });

        return view;
    }
}
