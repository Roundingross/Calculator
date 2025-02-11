package com.samcain.Calculator;

/**
 * States enum
 * IDLE - No input/waiting for input
 * LEFT_OPERAND - Collects left input
 * RIGHT_OPERAND - Collects right input
 * OPERATOR - Collects operator
 * DISPLAY - Displays result
 */
public enum States {
    IDLE, LEFT_OPERAND, RIGHT_OPERAND, OPERATOR, DISPLAY
}
