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
    private final int CHAIN_LENGTH = 9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ConstraintLayout mainLayout = binding.main;
        int[] viewIds = new int[CHAIN_LENGTH];

        for (int i = 0; i < CHAIN_LENGTH; i ++) {
            int id = View.generateViewId();
            TextView textView = new TextView(this);
            textView.setId(id);
            textView.setTag("textView" + i);
            textView.setText("TextView Chain Element " + i);
            textView.setTextSize(24);
            mainLayout.addView(textView);
            viewIds[i] = id;
        }

        ConstraintSet set = new ConstraintSet();
        set.clone(mainLayout);

        for (int id: viewIds) {
            set.connect(id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
            set.connect(id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);

            set.createVerticalChain(ConstraintSet.PARENT_ID, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, viewIds, null, ConstraintSet.CHAIN_PACKED);

            set.applyTo(mainLayout);
        }

        initLayout();
    }


    private void initLayout() {

    }
}