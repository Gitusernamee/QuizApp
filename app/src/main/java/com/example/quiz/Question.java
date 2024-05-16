package com.example.quiz;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Question extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView questionTextView;
    private RadioGroup answersRadioGroup;
    private Button nextButton;
    private String selectedCategory;
    private int questionIndex = 0;
    private String[] questions;
    private String[][] options;
    private TextView questionNum;
    private String[] answers;
    private int score = 0;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference userReferenceDb = FirebaseDatabase.getInstance().getReference("users");
    private int radioButtonId;
    private int scoreNN;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 15000; // 15 secondes
    private  RadioButton radio0,radio1,radio2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        questionTextView = findViewById(R.id.questionTextView);
        answersRadioGroup = findViewById(R.id.answersRadioGroup);
        nextButton = findViewById(R.id.nextButton);
        questionNum = findViewById(R.id.questionNum);
        timerTextView = findViewById(R.id.timerTextView);
        radio0 = findViewById(R.id.radio0);
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);

        radio0.setOnClickListener(view -> {
            radio0.setTextColor(Color.WHITE);
            radio1.setTextColor(Color.BLACK);
            radio2.setTextColor(Color.BLACK);
        });

        radio1.setOnClickListener(view -> {
            radio1.setTextColor(Color.WHITE);
            radio0.setTextColor(Color.BLACK);
            radio2.setTextColor(Color.BLACK);
        });

        radio2.setOnClickListener(view -> {
            radio2.setTextColor(Color.WHITE);
            radio0.setTextColor(Color.BLACK);
            radio1.setTextColor(Color.BLACK);
        });
        selectedCategory = getIntent().getStringExtra("category");
        if (selectedCategory == null) {
            Toast.makeText(this, "Catégorie non spécifiée", Toast.LENGTH_SHORT).show();
            finish();
        }

        loadQuestions(selectedCategory);

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {

                if (questionIndex < questions.length - 1) {
                    questionIndex++;
                    displayQuestion();
                    countDownTimer.start();
                } else {
                    updateUserScore();
                    finish();
                }
            }
        };

        nextButton.setOnClickListener(view -> {
            countDownTimer.cancel();


            radioButtonId = answersRadioGroup.getCheckedRadioButtonId();
            if (radioButtonId == -1) {
                Toast.makeText(this, "Veuillez sélectionner une réponse", Toast.LENGTH_SHORT).show();
                return;
            }


            RadioButton selectedRadioButton = findViewById(radioButtonId);
            if (selectedRadioButton != null) {
                String selectedAnswer = selectedRadioButton.getText().toString();


                String correctAnswer = answers[questionIndex];

                if (selectedAnswer.equals(correctAnswer)) {

                    if (timeLeftInMillis >= 10000) {
                        score += 3;
                        Toast.makeText(this, "Exellent", Toast.LENGTH_SHORT).show();
                    } else if (timeLeftInMillis >= 5000) {
                        score += 2;
                        Toast.makeText(this, "Bravooo", Toast.LENGTH_SHORT).show();
                    } else {
                        score += 1;
                        Toast.makeText(this, "C'est bon", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(this, "Ops", Toast.LENGTH_SHORT).show();
                }
                if (questionIndex < questions.length - 1) {
                    questionIndex++;
                    displayQuestion();
                    countDownTimer.start();
                } else {
                    updateUserScore();
                    finish();
                }
            } else {
                Toast.makeText(this, "Veuillez sélectionner une réponse", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadQuestions(String category) {
        mDatabase.child("quiz").child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
                questions = new String[size];
                options = new String[size][3];
                answers = new String[size];

                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String question = snapshot.child("question").getValue(String.class);
                    questions[i] = question;
                    String answer = snapshot.child("answer").getValue(String.class);
                    answers[i] = answer;
                    DataSnapshot optionsSnapshot = snapshot.child("options");
                    int j = 0;
                    for (DataSnapshot optionSnapshot : optionsSnapshot.getChildren()) {
                        options[i][j] = optionSnapshot.getValue(String.class);
                        j++;
                    }
                    i++;
                }


                displayQuestion();
                countDownTimer.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Question.this, "Erreur de récupération des questions", Toast.LENGTH_SHORT).show();
            }
        });
    }


private void displayQuestion() {
    questionTextView.setText(questions[questionIndex]);
    questionNum.setText("Question " + (questionIndex + 1));

    if (options != null && options.length > questionIndex) {
        radio0.setText(options[questionIndex][0]);
        radio1.setText(options[questionIndex][1]);
        radio2.setText(options[questionIndex][2]);
    }
}


private void updateUserScore() {
    if (user != null) {
        String userId = user.getUid();
        if (userId != null) {
            userReferenceDb.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int scoreN = dataSnapshot.child("quizs").child(selectedCategory).getValue(Integer.class);
                        scoreNN = scoreN;
                        if (score > scoreN) {
                            scoreNN = score;
                            userReferenceDb.child(userId).child("quizs").child(selectedCategory).setValue(score);
                            Toast.makeText(Question.this, "Alleeer nouveau score" + score, Toast.LENGTH_SHORT).show();

                            fetchUserScores();
                        } else {
                            Toast.makeText(Question.this, "Ops pas de nouveau score", Toast.LENGTH_SHORT).show();
                            fetchUserScores();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Question.this, "Erreur de récupération des données utilisateur", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("UserIDError", "L'ID de l'utilisateur est null.");
        }
    }
}

    private void fetchUserScores() {
        DatabaseReference userScoresRef = FirebaseDatabase.getInstance().getReference("users");
        userScoresRef.orderByChild("quizs/" + selectedCategory).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserScore> userScores = new ArrayList<>();
                int id = 1;
                int cpt = 0;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String username = userSnapshot.child("username").getValue(String.class);
                    int score = userSnapshot.child("quizs").child(selectedCategory).getValue(Integer.class);
                    userScores.add(new UserScore(id, username, score));
                    id++;
                    cpt++;
                }
                Collections.sort(userScores, (user1, user2) -> Integer.compare(user2.getBestScore(), user1.getBestScore()));
                userScores.forEach(userScore -> userScore.setId(userScores.indexOf(userScore) + 1));
                Intent intent = new Intent(Question.this, Score.class);
                intent.putParcelableArrayListExtra("userScores", (ArrayList<? extends Parcelable>) userScores);
                intent.putExtra("score1", score);
                intent.putExtra("score2", scoreNN);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Question.this, "Erreur de récupération des scores utilisateur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCountDownText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
        timerTextView.setText(timeLeftFormatted);
    }
}
