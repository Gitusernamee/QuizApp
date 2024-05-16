package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Score extends AppCompatActivity {

    TextView tvScoreActuel, tvScoreBest;
    ProgressBar pbBest,pbActuel;
    int scoreActuel, scoreBest;
    Button button, logOut;
    private RecyclerView recyclerView;
    private UserScoreAdapter adapter;
    ArrayList<UserScore> userScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);
        userScores = new ArrayList<>();
        tvScoreActuel = findViewById(R.id.scoreViewActuel);
        tvScoreBest = findViewById(R.id.scoreViewBest);
        pbBest = findViewById(R.id.progressBarCircleScoreBest);
        pbActuel = findViewById(R.id.progressBarCircleScoreActuel);
        button=findViewById(R.id.button);
        logOut = findViewById(R.id.logOut);

        Intent i1 = getIntent();
        scoreActuel = i1.getIntExtra("score2", 0);
        scoreBest = i1.getIntExtra("score1", 0);

        userScores = i1.getParcelableArrayListExtra("userScores");


        tvScoreActuel.setText(((scoreActuel * 100) / 15) + "%");
        pbActuel.setProgress(scoreActuel * 100 / 15);
        pbBest.setProgress(scoreBest * 100 / 15);
        tvScoreBest.setText(scoreBest * 100 / 15 + "%");

        adapter = new UserScoreAdapter(userScores);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Score.this, Themes.class);
                startActivity(intent);
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Score.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}

