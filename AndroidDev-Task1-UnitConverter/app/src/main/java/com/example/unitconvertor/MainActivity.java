package com.example.unitconvertor;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner categorySpinner, fromSpinner, toSpinner;
    EditText valueInput;
    Button convertButton;
    TextView resultText;

    String[] categories = {
            "Length",
            "Weight",
            "Temperature"
    };

    String[] lengthUnits = {
            "Centimetres",
            "Metres",
            "Kilometres",
            "Inches",
            "Feet"
    };

    String[] weightUnits = {
            "Grams",
            "Kilograms",
            "Pounds",
            "Ounces"
    };

    String[] temperatureUnits = {
            "Celsius",
            "Fahrenheit",
            "Kelvin"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect Java with XML components
        categorySpinner = findViewById(R.id.categorySpinner);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        valueInput = findViewById(R.id.valueInput);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);

        // Category Spinner
        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        categoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        categorySpinner.setAdapter(categoryAdapter);

        // Load units for the first category
        setUnits("Length");

        // Change units when category changes
        categorySpinner.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        String category =
                                categorySpinner.getSelectedItem().toString();

                        setUnits(category);

                        // Reset result
                        resultText.setText("Result will appear here");
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {
                    }
                });

        // Convert button
        convertButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String input = valueInput.getText().toString().trim();

                // Check empty input
                if (input.isEmpty()) {
                    Toast.makeText(
                            MainActivity.this,
                            "Please enter a value",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                double value;

                try {
                    value = Double.parseDouble(input);
                } catch (NumberFormatException e) {

                    Toast.makeText(
                            MainActivity.this,
                            "Please enter a valid number",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                String category =
                        categorySpinner.getSelectedItem().toString();

                String fromUnit =
                        fromSpinner.getSelectedItem().toString();

                String toUnit =
                        toSpinner.getSelectedItem().toString();

                double result = 0;

                if (category.equals("Length")) {

                    result = convertLength(
                            value,
                            fromUnit,
                            toUnit
                    );

                } else if (category.equals("Weight")) {

                    result = convertWeight(
                            value,
                            fromUnit,
                            toUnit
                    );

                } else if (category.equals("Temperature")) {

                    result = convertTemperature(
                            value,
                            fromUnit,
                            toUnit
                    );
                }

                resultText.setText(
                        String.format(
                                "%.4f %s = %.4f %s",
                                value,
                                fromUnit,
                                result,
                                toUnit
                        )
                );
            }
        });
    }

    // Set units according to category
    private void setUnits(String category) {

        String[] units;

        if (category.equals("Length")) {

            units = lengthUnits;

        } else if (category.equals("Weight")) {

            units = weightUnits;

        } else {

            units = temperatureUnits;
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
    }

    // Length conversion
    private double convertLength(
            double value,
            String from,
            String to) {

        // Convert everything to metres first

        double metres;

        switch (from) {

            case "Centimetres":
                metres = value / 100;
                break;

            case "Metres":
                metres = value;
                break;

            case "Kilometres":
                metres = value * 1000;
                break;

            case "Inches":
                metres = value * 0.0254;
                break;

            case "Feet":
                metres = value * 0.3048;
                break;

            default:
                metres = value;
        }

        // Convert metres to target unit

        switch (to) {

            case "Centimetres":
                return metres * 100;

            case "Metres":
                return metres;

            case "Kilometres":
                return metres / 1000;

            case "Inches":
                return metres / 0.0254;

            case "Feet":
                return metres / 0.3048;

            default:
                return metres;
        }
    }

    // Weight conversion
    private double convertWeight(
            double value,
            String from,
            String to) {

        // Convert everything to grams first

        double grams;

        switch (from) {

            case "Grams":
                grams = value;
                break;

            case "Kilograms":
                grams = value * 1000;
                break;

            case "Pounds":
                grams = value * 453.59237;
                break;

            case "Ounces":
                grams = value * 28.349523125;
                break;

            default:
                grams = value;
        }

        // Convert grams to target unit

        switch (to) {

            case "Grams":
                return grams;

            case "Kilograms":
                return grams / 1000;

            case "Pounds":
                return grams / 453.59237;

            case "Ounces":
                return grams / 28.349523125;

            default:
                return grams;
        }
    }

    // Temperature conversion
    private double convertTemperature(
            double value,
            String from,
            String to) {

        // First convert to Celsius

        double celsius;

        switch (from) {

            case "Celsius":
                celsius = value;
                break;

            case "Fahrenheit":
                celsius = (value - 32) * 5 / 9;
                break;

            case "Kelvin":
                celsius = value - 273.15;
                break;

            default:
                celsius = value;
        }

        // Celsius to target

        switch (to) {

            case "Celsius":
                return celsius;

            case "Fahrenheit":
                return (celsius * 9 / 5) + 32;

            case "Kelvin":
                return celsius + 273.15;

            default:
                return celsius;
        }
    }
}