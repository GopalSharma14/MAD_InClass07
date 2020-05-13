package com.example.group27_inclass07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView loading_tv;
    private TextView is_ready_tv;

    private Button exit_btn;
    private Button start_btn;

    private ImageView logo_iv;

    private ProgressBar progressbar;

    ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Trivia Quiz");

        loading_tv = findViewById(R.id.loading_tv);
        is_ready_tv = findViewById(R.id.is_ready_tv);
        exit_btn = findViewById(R.id.exit_btn);
        start_btn = findViewById(R.id.start_btn);
        logo_iv = findViewById(R.id.logo_iv);
        progressbar = findViewById(R.id.progressbar);

        start_btn.setEnabled(false);


        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new GetQuestions().execute();

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start_btn.isEnabled()) {
                    Intent intent = new Intent(MainActivity.this, TriviaActivity.class);
                    intent.putExtra("question_list", questions);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("question_list", questions);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    class GetQuestions extends AsyncTask<String, Void, ArrayList<Question>> {

        HttpURLConnection con = null;
        ArrayList<Question> qns_list = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Question> doInBackground(String... strings) {
            try {
                URL url = new URL("http://dev.theappsdr.com/apis/trivia_json/index.php");
                con = (HttpURLConnection) url.openConnection();
                con.connect();
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    String json = IOUtils.toString(con.getInputStream(), "UTF_8.");
                    JSONObject root = new JSONObject(json);
                    JSONArray qns = root.getJSONArray("questions");
                    for (int i = 0; i < qns.length(); i++) {
                        JSONObject qnJSON = qns.getJSONObject(i);

                        String question = qnJSON.getString("text");
                        String image_url = qnJSON.isNull("image") ? null : qnJSON.getString("image");
                        JSONObject choicesJSON = qnJSON.getJSONObject("choices");

                        JSONArray choice = choicesJSON.getJSONArray("choice");
                        ArrayList<String> choices = new ArrayList<>();
                        for (int j = 0; j < choice.length(); j++) {
                            choices.add(choice.getString(j));
                        }
                        int ans = choicesJSON.getInt("answer");

                        qns_list.add(new Question(question, image_url, choices, ans));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                con.disconnect();
            }
            return qns_list;
        }

        @Override
        protected void onPostExecute(ArrayList<Question> qns_list) {
            super.onPostExecute(questions);

            questions = qns_list;

            loading_tv.setVisibility(View.INVISIBLE);
            progressbar.setVisibility(View.INVISIBLE);
            logo_iv.setVisibility(View.VISIBLE);
            is_ready_tv.setVisibility(View.VISIBLE);
            start_btn.setEnabled(true);
        }
    }

}
