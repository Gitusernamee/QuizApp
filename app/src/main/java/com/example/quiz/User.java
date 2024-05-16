package com.example.quiz;

import com.google.firebase.auth.FirebaseUser;



public class User {
    private String username;
    private String email;

//    private Quizs quizs;
//    int score;
    private String UID;


    public User(){}
    public User(FirebaseUser user) {

    }


    public User(String username, String email) {
        this.username = username;
        this.email = email;
//        this.score=score;
    }
    public User(String UID){
        this.UID=UID;
    }


    // Getters et setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

