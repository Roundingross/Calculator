package com.samcain.Calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.samcain.Calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final int BUTTON_COUNT = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        initLayout();
    }

    // Initialize the layout
    private void initLayout() {
        ConstraintLayout constraintLayout = binding.main;
        int id = View.generateViewId();

        // Create output TextView
        TextView output = new TextView(this);
        output.setText("0");
        output.setId(id);
        output.setTextSize(24);
        constraintLayout.addView(output);

        // Create buttons
        int[] buttonIds = new int[BUTTON_COUNT];
        String[] buttonText = getResources().getStringArray(R.array.buttonNames);
        for (int i = 0; i < BUTTON_COUNT; i++) {
            TextView button = new TextView(this);
            button.setText(buttonText[i]);
            button.setId(buttonIds[i]);
            button.setTextSize(16);
            constraintLayout.addView(button);
        }
        // Connect buttons
        for (int i = 0; i < BUTTON_COUNT; i++) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(buttonIds[i], ConstraintSet.TOP, output.getId(), ConstraintSet.BOTTOM);
        }





    }
}