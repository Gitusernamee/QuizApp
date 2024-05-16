package com.example.quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserScoreAdapter extends RecyclerView.Adapter<UserScoreAdapter.ViewHolder> {
    private List<UserScore> userScoreList;

    public UserScoreAdapter(List<UserScore> userScores) {
        this.userScoreList=userScores;
    }

    // Constructeur et méthodes de mise à jour de la liste

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_score, parent, false);
        return new ViewHolder(view);
    }


@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    if (userScoreList != null && position < userScoreList.size()) {
        UserScore userScore = userScoreList.get(position);
        holder.textViewID.setText(String.valueOf(userScore.getId()));
        holder.textViewUsername.setText(userScore.getUsername());
        holder.textViewBestScore.setText(String.valueOf(userScore.getBestScore()));
    }
}


    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewID;
        TextView textViewUsername;
        TextView textViewBestScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewID = itemView.findViewById(R.id.textViewId);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewBestScore = itemView.findViewById(R.id.textViewBestScore);
        }
    }
}

