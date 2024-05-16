package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
    EditText name, mail, password, password1;
    Button register;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference qDatabase = FirebaseDatabase.getInstance().getReference("quiz");
    List<String> quizCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.editTextName);
        mail = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        password1 = findViewById(R.id.editTextTextConfirmPassword);
        register = findViewById(R.id.button);
        DataSnapshot dataSnapshot;

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nm = name.getText().toString();
                String email = mail.getText().toString();
                String pwd = password.getText().toString();
                String pwd1 = password1.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd1)) {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pwd.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Le mot de passe est trop court", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!pwd.equals(pwd1)) {
                    Toast.makeText(getApplicationContext(), "Les mots de passe ne correspondent pas", Toast.LENGTH_LONG).show();
                    return;
                }
                signUp(email, pwd, nm);

            }
        });
    }

    public void signUp(String email, String password, final String username) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Si l'inscription est réussie, ajoutez l'utilisateur à la base de données Firebase
                            Toast.makeText(getApplicationContext(), "Inscription réussie", Toast.LENGTH_LONG).show();
                            String userId = firebaseAuth.getCurrentUser().getUid(); // Utiliser getUid() pour obtenir l'ID de l'utilisateur
                            User user = new User(username, email); // Créer un nouvel utilisateur avec un score initial de 0 pour la catégorie "culture generale"
                            // Utilisez l'UID de l'utilisateur comme clé dans la base de données
                            mDatabase.child(userId).setValue(user);
                            initializeScores();
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Erreur lors de l'inscription: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    private void initializeScores(){
        qDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    quizCategories.add(categorySnapshot.getKey());
                }
                String userId = firebaseAuth.getCurrentUser().getUid();

                for(String categorie:quizCategories){
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("quizs").child(categorie).setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

