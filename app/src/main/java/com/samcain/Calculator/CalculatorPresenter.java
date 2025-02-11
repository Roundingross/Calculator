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
    private final CalculatorView view;

    /**
     * Constructor
     * @param view updates the view
     * @param context for model initialization
     */
    public CalculatorPresenter(CalculatorView view, Context context) {
        this.view = view;
        // Context for Toast messages
        this.model = new CalculatorModel(context);
    }
    /**
     * Handles user input and updates the view
     * @param input
     * '=' updates the secondary display
     * 'C' clears the secondary display
     */
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
