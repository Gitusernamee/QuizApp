package com.example.quiz;
import android.os.Parcel;
import android.os.Parcelable;

public class UserScore implements Parcelable {
    private int id;
    private String username;
    private int score;

    public UserScore(int id, String username, int score) {
        this.id = id;
        this.username = username;
        this.score = score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(int score) {
        this.score = score;
    }

    protected UserScore(Parcel in) {
        id = in.readInt();
        username = in.readString();
        score = in.readInt();
    }

    public static final Creator<UserScore> CREATOR = new Creator<UserScore>() {
        @Override
        public UserScore createFromParcel(Parcel in) {
            return new UserScore(in);
        }

        @Override
        public UserScore[] newArray(int size) {
            return new UserScore[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeInt(score);
    }

    public int getBestScore() {
        return score;
    }

}
