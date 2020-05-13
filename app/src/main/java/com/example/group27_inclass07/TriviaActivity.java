package com.example.group27_inclass07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity implements iChoiceListener {

    private Chip qn_chip;
    private Chip timer_chip;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ImageView qn_image_iv;

    private TextView question_tv;

    private Button next_btn;
    private Button quit_btn;

    private int counter;
    private int curr_answer;
    public static int correct_answers;

    private Question curr_question;

    ArrayList<Question> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        setTitle("Trivia Quiz");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        qn_chip = findViewById(R.id.qn_chip);
        timer_chip = findViewById(R.id.timer_chip);

        qn_image_iv = findViewById(R.id.qn_image_iv);

        question_tv = findViewById(R.id.question_tv);

        next_btn = findViewById(R.id.next_btn);
        quit_btn = findViewById(R.id.quit_btn);

        counter = 0;
        correct_answers = 0;

        new CountDownTimer(2 * 60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                long second = l / 1000;
                timer_chip.setText("Time left: " + second + " seconds");
            }

            @Override
            public void onFinish() {
                Toast.makeText(TriviaActivity.this, "Time's up!!", Toast.LENGTH_SHORT).show();
                next_btn.setEnabled(false);
                Log.d("demo", "correct answers = " + correct_answers);
                Intent send_score = new Intent(TriviaActivity.this, StatsActivity.class);
                send_score.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                send_score.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                send_score.putExtra("question_list", questions);
                send_score.putExtra("score", correct_answers);
                startActivity(send_score);
                finish();
            }
        }.start();

        Intent intent = getIntent();
        if (intent.hasExtra("question_list")) {
            questions = (ArrayList<Question>) intent.getSerializableExtra("question_list");
        }

        curr_question = questions.get(counter);

        ArrayList<String> choices = curr_question.choices;

        mAdapter = new ChoiceAdapter(choices, TriviaActivity.this);
        recyclerView.setAdapter(mAdapter);

        if (curr_question.image_url != null)
            Picasso.get().load(curr_question.image_url).into(qn_image_iv);
        else {
            qn_image_iv.setImageBitmap(null);
            Toast.makeText(TriviaActivity.this, "No image found", Toast.LENGTH_SHORT).show();
        }
        qn_chip.setText("Q" + String.valueOf(counter + 1));
        question_tv.setText(curr_question.question);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter < questions.size() - 1) {
                    curr_question = questions.get(++counter);

                    ArrayList<String> choices = curr_question.choices;

                    mAdapter = new ChoiceAdapter(choices, TriviaActivity.this);
                    recyclerView.setAdapter(mAdapter);

                    if (curr_question.image_url != null)
                        Picasso.get().load(curr_question.image_url).into(qn_image_iv);
                    else {
                        qn_image_iv.setImageBitmap(null);
                        Toast.makeText(TriviaActivity.this, "No image found", Toast.LENGTH_SHORT).show();
                    }
                    qn_chip.setText("Q" + String.valueOf(counter + 1));
                    question_tv.setText(curr_question.question);
                } else {
                    Toast.makeText(TriviaActivity.this, "Trivia successfully completed!", Toast.LENGTH_SHORT).show();
                    next_btn.setEnabled(false);
                    Log.d("demo", "correct answers = " + correct_answers);
                    Intent send_score = new Intent(TriviaActivity.this, StatsActivity.class);
                    send_score.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    send_score.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    send_score.putExtra("question_list", questions);
                    send_score.putExtra("score", correct_answers);
                    startActivity(send_score);
                    finish();
                }
            }
        });

        quit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_main = new Intent(TriviaActivity.this, MainActivity.class);
                go_to_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                go_to_main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(go_to_main);
                finish();
            }
        });
    }

    @Override
    public void onChoiceClick(int pos) {
        Log.d("demo", "clicked " + curr_question.choices.get(pos) + " " + (pos + 1) + " correct answer= " + curr_question.ans);

        curr_answer = pos + 1;

        if (curr_answer == curr_question.ans) {
            correct_answers += 1;
            Log.d("demo", "correct answers = " + correct_answers);
            curr_answer = -1;
        }

        if (counter < questions.size() - 1) {
            curr_question = questions.get(++counter);

            ArrayList<String> choices = curr_question.choices;

            mAdapter = new ChoiceAdapter(choices, TriviaActivity.this);
            recyclerView.setAdapter(mAdapter);

            if (curr_question.image_url != null)
                Picasso.get().load(curr_question.image_url).into(qn_image_iv);
            else {
                qn_image_iv.setImageBitmap(null);
                Toast.makeText(TriviaActivity.this, "No image found", Toast.LENGTH_SHORT).show();
            }
            qn_chip.setText("Q" + String.valueOf(counter + 1));
            question_tv.setText(curr_question.question);
        } else {
            Toast.makeText(TriviaActivity.this, "Trivia successfully completed!", Toast.LENGTH_SHORT).show();
            next_btn.setEnabled(false);
            Log.d("demo", "correct answers = " + correct_answers);
            Intent send_score = new Intent(TriviaActivity.this, StatsActivity.class);
            send_score.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            send_score.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            send_score.putExtra("question_list", questions);
            send_score.putExtra("score", correct_answers);
            startActivity(send_score);
            finish();
        }
    }
}
