package com.samcain.Calculator;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * CalculatorModel class
 * Handles all the logic for operations and data storage.
 * Calculates results, state management, and display updates.
 */
public class CalculatorModel {
    // States
    private States currentState;
    // Context for displaying Toast messages
    private final Context context;
    // Operands, operator, and full expression
    private final StringBuilder leftOperand;
    private final StringBuilder rightOperand;
    private String operator;
    private final StringBuilder expression;
    // Main display
    private String display;

    /**
     * Constructor
     * @param context for displaying Toast messages
     */
    public CalculatorModel(Context context) {
        this.context = context;
        currentState = States.IDLE;
        leftOperand = new StringBuilder();
        rightOperand = new StringBuilder();
        expression = new StringBuilder();
        operator = "";
        display = "0";
    }

    // Processes user input and update display
    public String processInput(String input) {
        if (isUnaryOperator(input)) {
            processUnaryOperator(input);
        } else if (isDigitOrDecimal(input)) {
            processDigit(input);
        } else if (isBinaryOperator(input)) {
            processBinaryOperator(input);
        } else if (input.equals("=")) {
            operationCheck();
        } else if (input.equals("C")) {
            clearAll();
        } else if (input.equals("±")) {
            toggleSign();
        }
        return display;
    }

    // Handles digit and decimal input
    private void processDigit(String digit) {
        StringBuilder currentOperand = (currentState == States.RIGHT_OPERAND) ? rightOperand : leftOperand;
        // Prevent multiple decimal points in a number
        if (digit.equals(".") && currentOperand.toString().contains(".")) {
            showToast("Only one decimal point is allowed.");
            return;
        }
        switch (currentState) {
            case ERROR:
            case IDLE:
            case DISPLAY:
                // If a new digit is typed after a calculation, reset everything
                resetExpression();
                leftOperand.setLength(0);
                leftOperand.append(digit);
                updateExpression(leftOperand.toString());
                currentState = States.LEFT_OPERAND;
                break;
            // Handle left operand
            case LEFT_OPERAND:
                leftOperand.append(digit);
                updateExpression(leftOperand.toString());
                break;
            case OPERATOR:
                // Clear right operand before entering
                rightOperand.setLength(0);
                rightOperand.append(digit);
                updateExpression(leftOperand + " " + operator + " " + rightOperand);
                currentState = States.RIGHT_OPERAND;
                break;
            // Handle right operand
            case RIGHT_OPERAND:
                rightOperand.append(digit);
                updateExpression(leftOperand + " " + operator + " " + rightOperand);
                break;
        }
    }

    // Handles binary operator input
    private void processBinaryOperator(String op) {
        // Convert dash to minus
        if (op.equals("−")) {
            op = "-";
        }
        switch (currentState) {
            // Handle left operand
            case LEFT_OPERAND:
            case DISPLAY:
                operator = op;
                updateExpression(leftOperand + " " + operator);
                currentState = States.OPERATOR;
                break;
            // Handle operator
            case OPERATOR:
                operator = op;
                updateExpression(leftOperand + " " + operator);
                break;
            // Handle right operand
            case RIGHT_OPERAND:
                calculateResult();
                operator = op;
                updateExpression(leftOperand + " " + operator);
                currentState = States.OPERATOR;
                break;
        }
    }

    // Handles unary operators (√, %)
    private void processUnaryOperator(String op) {
        if (leftOperand.length() == 0) {
            showToast("No operand available");
            display = "Error";
            currentState = States.ERROR;
            return;
        }
        try {
            BigDecimal operand = new BigDecimal(leftOperand.toString());
            BigDecimal result;
            switch (op) {
                // Square-root
                case "√":
                    if (operand.compareTo(BigDecimal.ZERO) < 0) {
                        showToast("Cannot take square root of a negative number");
                        display = "Error";
                        currentState = States.ERROR;
                        return;
                    }
                    result = new BigDecimal(Math.sqrt(operand.doubleValue()), new MathContext(10, RoundingMode.HALF_UP));
                    break;
                // Percentage
                case "%":
                    result = operand.divide(new BigDecimal("100"), new MathContext(10, RoundingMode.HALF_UP));
                    break;
                // Unsupported operation
                default:
                    return;
            }
            // Update display
            leftOperand.setLength(0);
            leftOperand.append(result.stripTrailingZeros().toEngineeringString());
            updateExpression(leftOperand.toString());
            currentState = States.DISPLAY;
        } catch (Exception e) {
            // Handle exceptions
            showToast("Unary operation error");
            display = "Error";
            currentState = States.ERROR;
        }
    }

    /**
     * Checks if the expression is valid and performs the calculation
     * Stores right operand for repeated calculations with "op" or "="
     *
     * Set work for updating secondary display on repeated operations
     */
    private void operationCheck() {
        if (leftOperand.length() == 0 || operator.isEmpty()) {
            showToast("Incomplete expression");
            display = "Error";
            currentState = States.ERROR;
        } else if (rightOperand.length() == 0) {
            // Store right operand
            rightOperand.setLength(0);
            rightOperand.append(leftOperand);
            calculateResult();
        } else {
            calculateResult();
        }
    }

    /**
     * Performs calculation and updates display
     * Stores expression history for display
     */
    private void calculateResult() {
        try {
            MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
            BigDecimal left, right, result;
            // Handle special cases
            if (currentState == States.DISPLAY) {
                if (operator.isEmpty() || rightOperand.length() == 0) {
                    return;
                }
                left = new BigDecimal(leftOperand.toString());
                right = new BigDecimal(rightOperand.toString());
            } else {
                left = new BigDecimal(leftOperand.toString());
                right = new BigDecimal(rightOperand.toString());

                // Store right operand for repeated calculations with "="
                rightOperand.setLength(0);
                rightOperand.append(right.toPlainString());
            }
            switch (operator) {
                // Plus
                case "+":
                    result = left.add(right, mc);
                    break;
                // Minus
                case "-":
                    result = left.subtract(right, mc);
                    break;
                // Multiply
                case "×":
                    result = left.multiply(right, mc);
                    break;
                // Divide
                case "÷":
                    if (right.compareTo(BigDecimal.ZERO) == 0) {
                        showToast("Cannot divide by zero");
                        display = "Error";
                        currentState = States.ERROR;
                        return;
                    }
                    result = left.divide(right, mc);
                    break;
                // Unsupported operator
                default:
                    showToast("Unsupported operator");
                    display = "Error";
                    currentState = States.ERROR;
                    return;
            }
            // Format left number
            String formattedLeft = formatNumber(left.toPlainString());
            expression.setLength(0);
            expression.append(formattedLeft).append(" ").append(operator).append(" ").append(right.toPlainString()).append(" = ");
            // Update leftOperand with the new result
            leftOperand.setLength(0);
            leftOperand.append(result.stripTrailingZeros().toPlainString());
            updateExpression(leftOperand.toString());
            currentState = States.DISPLAY;
        } catch (Exception e) {
            // Handle exceptions
            showToast("Calculation error");
            Log.d("DEBUG", "Exception in calculateResult(): " + e.getMessage());
            display = "Error";
            currentState = States.ERROR;
        }
    }

    // Helpers
    private boolean isDigitOrDecimal(String input) {
        return input.matches("[0-9.]");
    }
    private boolean isBinaryOperator(String input) {
        return input.matches("[+\\-×÷−]");
    }
    private boolean isUnaryOperator(String input) {
        return input.equals("√") || input.equals("%");
    }

    // Clear all variables
    private void clearAll() {
        currentState = States.IDLE;
        leftOperand.setLength(0);
        rightOperand.setLength(0);
        operator = "";
        expression.setLength(0);
        display = "0";
    }
    private void resetExpression() {
        expression.setLength(0);
    }

    // Update display
    private void updateExpression(String updatedExpression) {
        // Only format if it's a valid number
        try {
            // Try parsing as a number
            new BigDecimal(updatedExpression);
            display = formatNumber(updatedExpression);
        } catch (NumberFormatException e) {
            display = updatedExpression;
        }
    }

    // Format number
    private String formatNumber(String value) {
        try {
            // Remove unnecessary commas or spaces
            String sanitizedValue = value.replace(",", "").trim();
            BigDecimal num = new BigDecimal(sanitizedValue);
            // Convert to Scientific Notation after 12 digits
            int digits = num.precision();
            if (digits > 12 || sanitizedValue.length() > 12) {
                return new DecimalFormat("0.0E0").format(num);
            } else {
                // Format with commas for large numbers
                NumberFormat formatter = NumberFormat.getInstance();
                formatter.setGroupingUsed(true);
                return formatter.format(num);
            }
        } catch (Exception e) {
            Log.e("DEBUG", "Number formatting error - " + value, e);
            return value;
        }
    }

    // Plus/Minus
    private void toggleSign() {
        switch (currentState) {
            case LEFT_OPERAND:
                if (leftOperand.length() > 0) {
                    if (leftOperand.charAt(0) == '-') {
                        leftOperand.deleteCharAt(0);
                    } else {
                        leftOperand.insert(0, '-');
                    }
                    updateExpression(leftOperand.toString());
                }
                break;
            case RIGHT_OPERAND:
                if (rightOperand.length() > 0) {
                    if (rightOperand.charAt(0) == '-') {
                        rightOperand.deleteCharAt(0);
                    } else {
                        rightOperand.insert(0, '-');
                    }
                    updateExpression(leftOperand + " " + operator + " " + rightOperand);
                }
                break;
        }
    }

    // Toast helper
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Getter
    public String getFullExpression() {
        return expression.toString();
    }
}