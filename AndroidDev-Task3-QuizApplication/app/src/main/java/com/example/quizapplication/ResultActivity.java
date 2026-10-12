
package com.example.quizapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvCorrect = findViewById(R.id.tvCorrect);
        TextView tvIncorrect = findViewById(R.id.tvIncorrect);
        Button btnRestart = findViewById(R.id.btnRestart);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 10);

        tvScore.setText("Your Score: " + score + " / " + total);
        tvCorrect.setText("Correct Answers: " + score);
        tvIncorrect.setText("Incorrect Answers: " + (total - score));

        btnRestart.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ResultActivity.this,
                    MainActivity.class
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);
            finish();
        });
    }
}
