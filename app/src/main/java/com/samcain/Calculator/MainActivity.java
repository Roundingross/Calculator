package com.samcain.Calculator;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import com.samcain.Calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    // Model pass for Toast context
    private final CalculatorModel model = new CalculatorModel(this);
    // Click Handler
    private ClickHandler clickHandler;
    // Output TextView
    private TextView outputDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        clickHandler = new ClickHandler();
        initLayout();
    }

    // Initialize Layout
    private void initLayout() {
        // Get ConstraintLayout
        ConstraintLayout constraintLayout = binding.main;
        // Set background color
        constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
        // Initialize variables/arrays
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        int buttonIndex = 0;
        int KEY_ROWS = 4;
        int KEY_COLUMNS = 5;

        // Get button text/tags from Arrays
        String[] buttonText = getResources().getStringArray(R.array.buttonText);
        String[] buttonTag = getResources().getStringArray(R.array.buttonTags);

        // Create arrays for horizontal and vertical chains.
        int[][] horizontals = new int[KEY_ROWS][KEY_COLUMNS];
        int[][] verticals = new int[KEY_COLUMNS][KEY_ROWS];

        // Create output TextView
        outputDisplay = new TextView(this);
        int outputId = View.generateViewId();
        outputDisplay.setId(outputId);
        outputDisplay.setTag("output");
        outputDisplay.setTextSize(48);
        outputDisplay.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        constraintLayout.addView(outputDisplay);

        // Create button grid.
        for (int i = 0; i < KEY_ROWS; i++) {
            for (int j = 0; j < KEY_COLUMNS; j++) {
                // Break if all buttons have been added
                if (buttonIndex >= buttonText.length) {
                    break;
                }
                // Create buttons
                Button button = new Button(this);
                int buttonId = View.generateViewId();
                button.setId(buttonId);
                button.setText(buttonText[buttonIndex]);
                button.setTextSize(24);
                button.setTag(buttonTag[buttonIndex]);
                // Add buttons to arrays
                horizontals[i][j] = buttonId;
                verticals[j][i] = buttonId;
                // Set click handler
                button.setOnClickListener(clickHandler);
                // Add buttons to ConstraintLayout
                constraintLayout.addView(button);
                buttonIndex++;
            }
        }

        // Create ConstraintSet
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        // Constrain the output TextView
        constraintSet.connect(outputId, ConstraintSet.TOP, binding.guidelineNorth.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(outputId, ConstraintSet.END, binding.guidelineEast.getId(), ConstraintSet.START);
        constraintSet.constrainWidth(outputId, ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(outputId, ConstraintSet.WRAP_CONTENT);

        // Set button size and margin
        for (int i = 0; i < KEY_ROWS; i++) {
            for (int j = 0; j < KEY_COLUMNS; j++) {
                int buttonId = horizontals[i][j];
                constraintSet.constrainWidth(buttonId, 0);
                constraintSet.constrainHeight(buttonId, 0);
                constraintSet.setMargin(buttonId, ConstraintSet.TOP, margin);
                constraintSet.setMargin(buttonId, ConstraintSet.BOTTOM, margin);
                constraintSet.setMargin(buttonId, ConstraintSet.LEFT, margin);
                constraintSet.setMargin(buttonId, ConstraintSet.RIGHT, margin);
            }
        }

        // Create horizontal chains
        for (int row = 0; row < KEY_ROWS; row++) {
            constraintSet.createHorizontalChain(
                    binding.guidelineWest.getId(), ConstraintSet.RIGHT, binding.guidelineEast.getId(), ConstraintSet.LEFT,
                    horizontals[row], null, ConstraintSet.CHAIN_SPREAD_INSIDE
            );
        }

        // Create vertical chains
        for (int col = 0; col < KEY_COLUMNS; col++) {
            constraintSet.createVerticalChain(
                    outputId, ConstraintSet.BOTTOM, binding.guidelineSouth.getId(), ConstraintSet.TOP,
                    verticals[col],null, ConstraintSet.CHAIN_SPREAD_INSIDE
            );
        }
        constraintSet.applyTo(constraintLayout);
    }

    // Click handler class
    private class ClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String input = view.getTag().toString();
            final String output = model.processInput(input);

            runOnUiThread(() -> outputDisplay.setText(output));
        }
    }
}