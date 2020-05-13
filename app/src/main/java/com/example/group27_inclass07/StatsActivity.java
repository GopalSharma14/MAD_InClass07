package com.example.group27_inclass07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    private TextView percent_tv;

    private ProgressBar hz_progressbar;

    private Button quit_btn;
    private Button try_again_btn;

    private ArrayList<Question> questions;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setTitle("Trivia Quiz");

        percent_tv = findViewById(R.id.percent_tv);
        hz_progressbar = findViewById(R.id.hz_progressbar);
        quit_btn = findViewById(R.id.quit_btn);
        try_again_btn = findViewById(R.id.try_again_btn);


        Intent receive_score = getIntent();
        Log.d("demo", "after intent in stats");
        if (receive_score.hasExtra("question_list")) {
            questions = (ArrayList<Question>) receive_score.getSerializableExtra("question_list");
        }
        if (receive_score.hasExtra("score")) {
            score = (int) receive_score.getIntExtra("score", 0);
        }

        percent_tv.setText((int) ((double) score / (double) questions.size() * 100) + "%");
        hz_progressbar.setProgress((int) ((double) score / (double) questions.size() * 100));

        if (score >= questions.size()) {
            TextView ta_tv = findViewById(R.id.try_again_tv);
            ta_tv.setText("Good Job, You got all the questions right!\n\nPress Quit to Exit");
            try_again_btn.setEnabled(false);
        }


        quit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (score >= questions.size()) {
                    Log.d("demo", "inside quit button inside score > size");
                    finish();
                }
                else {
                    Intent go_to_main = new Intent(StatsActivity.this, MainActivity.class);
                    go_to_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    go_to_main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(go_to_main);
                    finish();
                }
            }
        });

        try_again_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatsActivity.this, TriviaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("question_list", questions);
                startActivity(intent);
                finish();
            }
        });
    }
}
