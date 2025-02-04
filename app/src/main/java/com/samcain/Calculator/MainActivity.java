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
    private final int KEY_HEIGHT = 4;
    private final int KEY_WIDTH = 5;

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

        int buttonIndex = 0;
        for (int i = 0; i < KEY_HEIGHT; i++) {
            for (int j = 0; j < KEY_WIDTH; j++) {
                // Create button
                TextView button = new TextView(this);
                button.setText(getResources().getStringArray(R.array.buttons)[buttonIndex]);
                button.setTextSize(24);
                button.setId(View.generateViewId());
                constraintLayout.addView(button);
                buttonIndex++;
            }
        }

        // Create chain of buttons
        int[][] horizontalChains = new int[KEY_HEIGHT][KEY_WIDTH];
        int[][] verticalChains = new int[KEY_HEIGHT][KEY_WIDTH];
        for (int i = 0; i < KEY_HEIGHT; i++) {
            for (int j = 0; j < KEY_WIDTH; j++) {
                horizontalChains[i][j] = View.generateViewId();
                verticalChains[i][j] = View.generateViewId();
            }
        }










    }
}