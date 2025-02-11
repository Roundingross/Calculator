package com.samcain.Calculator;

import android.content.Context;

// Presenter for handling communication between View (MainActivity) and Model
public class CalculatorPresenter {
    private final CalculatorModel model;
    private final CalculatorView view;

    public CalculatorPresenter(CalculatorView view, Context context) {
        this.view = view;
        // Context for Toast messages
        this.model = new CalculatorModel(context);
    }
    public void onButtonClick(String input) {
        String result = model.processInput(input);
        if (input.equals("=")) {
            String fullExpression = model.getFullExpression();

            ((MainActivity) view).updateSecondaryDisplay(fullExpression);
        }
        if (input.equals("C")) {
            ((MainActivity) view).updateSecondaryDisplay("");
        }
        view.updateDisplay(result);
    }

}
