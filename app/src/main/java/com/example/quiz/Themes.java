package com.example.quiz;
// Themes.java
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Themes extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("quiz");

        final LinearLayout buttonContainer = findViewById(R.id.button_container);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> quizCategories = new ArrayList<>();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    quizCategories.add(categorySnapshot.getKey());
                }

                for (final String category : quizCategories) {
                    Button button = new Button(Themes.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 0, 16);
                    button.setLayoutParams(params);
                    button.setText(category);

                    button.setBackgroundResource(R.drawable.themebg);
                    button.setTextColor(getResources().getColor(R.color.colorPrimary));
                    button.setGravity(Gravity.CENTER);
                    button.setTextSize(15);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String category = button.getText().toString();
                            Intent intent = new Intent(Themes.this, Question.class);
                            intent.putExtra("category", category);
                            startActivity(intent);
                        }
                    });
                    LinearLayout buttonContainer = findViewById(R.id.button_container);
                    buttonContainer.setGravity(Gravity.CENTER_HORIZONTAL); // Centrer les boutons horizontalement
                    buttonContainer.addView(button);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
