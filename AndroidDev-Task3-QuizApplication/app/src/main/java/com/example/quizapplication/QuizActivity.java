package com.example.quizapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {

    TextView tvQuestion, tvCounter;
    RadioGroup rgOptions;
    RadioButton rb1, rb2, rb3, rb4;
    Button btnNext;

    String[] questions = {
            "What is the capital of India?",
            "Which planet is known as the Red Planet?",
            "How many days are there in a leap year?",
            "Which is the largest ocean on Earth?",
            "Who wrote the Indian national anthem?",
            "What is the chemical symbol for gold?",
            "Which is the fastest land animal?",
            "How many sides does a hexagon have?",
            "Which gas do plants absorb?",
            "What is the largest mammal in the world?"
    };

    String[][] options = {
            {"Mumbai", "New Delhi", "Chennai", "Kolkata"},
            {"Venus", "Jupiter", "Mars", "Mercury"},
            {"364", "365", "366", "367"},
            {"Atlantic", "Indian", "Arctic", "Pacific"},
            {"Rabindranath Tagore", "Mahatma Gandhi",
                    "Jawaharlal Nehru", "Sarojini Naidu"},
            {"Ag", "Au", "Fe", "Cu"},
            {"Lion", "Cheetah", "Horse", "Tiger"},
            {"Five", "Six", "Seven", "Eight"},
            {"Oxygen", "Nitrogen", "Carbon dioxide", "Hydrogen"},
            {"Elephant", "Giraffe", "Blue whale", "Hippopotamus"}
    };

    int[] answers = {1, 2, 2, 3, 0, 1, 1, 1, 2, 2};

    ArrayList<Integer> questionOrder = new ArrayList<>();
    int currentQuestion = 0;
    int score = 0;
    boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvCounter = findViewById(R.id.tvCounter);
        rgOptions = findViewById(R.id.rgOptions);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        btnNext = findViewById(R.id.btnNext);

        for (int i = 0; i < questions.length; i++) {
            questionOrder.add(i);
        }

        Collections.shuffle(questionOrder);

        // Hide options while the initial question is prepared.
        rgOptions.setVisibility(View.INVISIBLE);

        loadQuestion();

        btnNext.setOnClickListener(v -> {
            if (!answered) {
                Toast.makeText(this,
                        "Please select an answer",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            currentQuestion++;

            if (currentQuestion < questions.length) {
                loadQuestion();
            } else {
                Intent intent = new Intent(
                        QuizActivity.this, ResultActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("total", questions.length);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuestion() {
        answered = false;

        // Temporarily hide options during reset.
        rgOptions.setVisibility(View.INVISIBLE);

        // Prevent the listener from reacting to reset operations.
        rgOptions.setOnCheckedChangeListener(null);

        // Clear any selection from the previous question.
        rgOptions.clearCheck();

        RadioButton[] buttons = {rb1, rb2, rb3, rb4};

        for (RadioButton button : buttons) {
            button.setChecked(false);
            button.setEnabled(true);
            button.setTextColor(Color.BLACK);
            button.setBackgroundColor(Color.TRANSPARENT);
        }

        int index = questionOrder.get(currentQuestion);

        tvCounter.setText("Question " + (currentQuestion + 1)
                + " of " + questions.length);

        tvQuestion.setText(questions[index]);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(options[index][i]);
        }

        btnNext.setText(currentQuestion == questions.length - 1
                ? "View Results" : "Next");

        // Restore answer selection handling.
        rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1 && !answered) {
                checkAnswer(checkedId);
            }
        });

        // Show the refreshed options after the current UI update.
        rgOptions.post(() -> {
            rgOptions.clearCheck();
            rgOptions.setVisibility(View.VISIBLE);
        });
    }

    private void checkAnswer(int checkedId) {
        if (answered) {
            return;
        }

        RadioButton[] buttons = {rb1, rb2, rb3, rb4};
        int selected = -1;

        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getId() == checkedId) {
                selected = i;
                break;
            }
        }

        if (selected == -1) {
            return;
        }

        answered = true;

        int index = questionOrder.get(currentQuestion);
        int correct = answers[index];

        if (selected == correct) {
            score++;
            buttons[selected].setBackgroundColor(
                    Color.parseColor("#A5D6A7"));
        } else {
            // Display the selected wrong answer in red.
            buttons[selected].setBackgroundColor(
                    Color.parseColor("#EF9A9A"));

            // Display the correct answer in green.
            buttons[correct].setBackgroundColor(
                    Color.parseColor("#A5D6A7"));
        }

        // Keep the answer colors visible until Next is tapped.
        for (RadioButton button : buttons) {
            button.setEnabled(false);
        }
    }}