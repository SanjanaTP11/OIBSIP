package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView display;
    StringBuilder expression = new StringBuilder();
    boolean resultShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
        GridLayout grid = findViewById(R.id.buttonGrid);

        for (int i = 0; i < grid.getChildCount(); i++) {
            View view = grid.getChildAt(i);

            if (view instanceof Button) {
                Button button = (Button) view;
                button.setOnClickListener(this::handleClick);
            }
        }
    }

    private void handleClick(View view) {
        Button button = (Button) view;
        String value = button.getText().toString();

        switch (value) {
            case "C":
                expression.setLength(0);
                resultShown = false;
                display.setText("0");
                break;

            case "⌫":
                if (resultShown) {
                    expression.setLength(0);
                    resultShown = false;
                } else if (expression.length() > 0) {
                    expression.deleteCharAt(expression.length() - 1);
                }
                updateDisplay();
                break;

            case "=":
                calculate();
                break;

            case "+":
            case "−":
            case "×":
            case "÷":
                appendOperator(value);
                break;

            case ".":
                appendDecimal();
                break;

            default:
                if (value.matches("[0-9]")) {
                    if (resultShown) {
                        expression.setLength(0);
                        resultShown = false;
                    }
                    expression.append(value);
                    updateDisplay();
                }
                break;
        }
    }

    private void appendOperator(String operator) {
        if (expression.length() == 0) {
            if (operator.equals("−")) {
                expression.append("-");
                updateDisplay();
            }
            return;
        }

        char last = expression.charAt(expression.length() - 1);

        if (isOperator(last)) {
            expression.setCharAt(expression.length() - 1,
                    operator.charAt(0));
        } else {
            expression.append(operator);
        }

        resultShown = false;
        updateDisplay();
    }

    private void appendDecimal() {
        if (resultShown) {
            expression.setLength(0);
            resultShown = false;
        }

        int i = expression.length() - 1;

        while (i >= 0 && !isOperator(expression.charAt(i))) {
            if (expression.charAt(i) == '.') {
                return;
            }
            i--;
        }

        if (expression.length() == 0 ||
                isOperator(expression.charAt(expression.length() - 1))) {
            expression.append("0.");
        } else {
            expression.append(".");
        }

        updateDisplay();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '−' || c == '×' || c == '÷'
                || c == '-' || c == '*' || c == '/';
    }

    private void updateDisplay() {
        display.setText(expression.length() == 0
                ? "0" : expression.toString());
    }

    private void calculate() {
        try {
            String input = expression.toString()
                    .replace('−', '-')
                    .replace('×', '*')
                    .replace('÷', '/');

            if (input.isEmpty() ||
                    isOperator(input.charAt(input.length() - 1))) {
                display.setText("Error");
                expression.setLength(0);
                resultShown = true;
                return;
            }

            double result = evaluate(input);

            if (Double.isNaN(result) || Double.isInfinite(result)) {
                throw new ArithmeticException();
            }

            String answer;
            if (result == Math.floor(result)
                    && Math.abs(result) < 1e15) {
                answer = String.valueOf((long) result);
            } else {
                answer = String.valueOf(result);
            }

            display.setText(answer);
            expression.setLength(0);
            expression.append(answer);
            resultShown = true;

        } catch (Exception e) {
            display.setText("Error");
            expression.setLength(0);
            resultShown = true;
        }
    }

    private double evaluate(String input) {
        java.util.ArrayList<Double> numbers = new java.util.ArrayList<>();
        java.util.ArrayList<Character> operators = new java.util.ArrayList<>();

        int i = 0;

        while (i < input.length()) {
            int start = i;

            if (input.charAt(i) == '-' &&
                    (i == 0 || isOperator(input.charAt(i - 1)))) {
                i++;
            }

            while (i < input.length() &&
                    (Character.isDigit(input.charAt(i))
                            || input.charAt(i) == '.')) {
                i++;
            }

            if (start == i) {
                throw new IllegalArgumentException();
            }

            numbers.add(Double.parseDouble(input.substring(start, i)));

            if (i < input.length()) {
                char op = input.charAt(i);
                if (!isOperator(op)) {
                    throw new IllegalArgumentException();
                }
                operators.add(op);
                i++;
            }
        }

        for (int j = 0; j < operators.size();) {
            char op = operators.get(j);

            if (op == '*' || op == '/') {
                double a = numbers.get(j);
                double b = numbers.get(j + 1);

                if (op == '/' && b == 0) {
                    throw new ArithmeticException();
                }

                double value = op == '*' ? a * b : a / b;
                numbers.set(j, value);
                numbers.remove(j + 1);
                operators.remove(j);
            } else {
                j++;
            }
        }

        double result = numbers.get(0);

        for (int j = 0; j < operators.size(); j++) {
            double next = numbers.get(j + 1);

            if (operators.get(j) == '+') {
                result += next;
            } else if (operators.get(j) == '-') {
                result -= next;
            } else {
                throw new IllegalArgumentException();
            }
        }

        return result;
    }
}
