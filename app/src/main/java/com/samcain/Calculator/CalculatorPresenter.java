package com.samcain.Calculator;

import android.content.Context;

/**
 * CalculatorPresenter class
 * Handles user input and updates the view.
 * Bridges communication between the view and the model.
 * Forwards user input to the model and updates the view accordingly.
 */
public class CalculatorPresenter {
    private final CalculatorModel model;
    private final MainActivity view;

    /**
     * Constructor
     * @param view updates the view
     * @param context for model initialization
     */
    public CalculatorPresenter(MainActivity view, Context context) {
        // Context for Toast messages
        this.model = new CalculatorModel(context);
        this.view = view;
    }
    /**
     * Handles user input and updates the view
     * @param input
     * '=' updates the main display and secondary display
     * 'C' clears the main display and secondary display
     */
    public void onButtonClick(String input) {
        // Pass input to the model
        String result = model.processInput(input);
        if (input.equals("=")) {
            String fullExpression = model.getFullExpression();

            (view).updateSecondaryDisplay(fullExpression);
        }
        if (input.equals("C")) {
            (view).updateSecondaryDisplay("");
        }
        // Update the view with the result
        view.updateDisplay(result);
    }

}
