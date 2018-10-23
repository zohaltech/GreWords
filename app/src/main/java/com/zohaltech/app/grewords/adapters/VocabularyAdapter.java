package com.zohaltech.app.grewords.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.activities.VocabularyDetailsActivity;
import com.zohaltech.app.grewords.entities.Vocabulary;

import java.util.ArrayList;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.ViewHolder> {
    
    private Activity              activity;
    private ArrayList<Vocabulary> vocabularies;
    
    public VocabularyAdapter(Activity activity, ArrayList<Vocabulary> vocabularies) {
        this.activity = activity;
        this.vocabularies = vocabularies;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_vocabulary, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Vocabulary vocabulary = vocabularies.get(position);
        if (vocabulary.getLearned()) {
            holder.imgLearned.setVisibility(View.VISIBLE);
        } else {
            holder.imgLearned.setVisibility(View.GONE);
        }
        
        holder.txtVocabulary.setText(vocabulary.getVocabulary());
        holder.layoutVocabulary.setOnClickListener(v -> {
            Intent intent = new Intent(activity, VocabularyDetailsActivity.class);
            intent.putExtra(VocabularyDetailsActivity.VOCAB_ID, vocabulary.getId());
            intent.putExtra(VocabularyDetailsActivity.INIT_MODE_KEY, VocabularyDetailsActivity.MODE_VIEW);
            activity.startActivity(intent);
        });
    }
    
    @Override
    public int getItemCount() {
        return vocabularies.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView           txtVocabulary;
        public AppCompatImageView imgLearned;
        LinearLayoutCompat layoutVocabulary;
        
        ViewHolder(View view) {
            super(view);
            layoutVocabulary = view.findViewById(R.id.layoutVocabulary);
            txtVocabulary = view.findViewById(R.id.txtVocabulary);
            imgLearned = view.findViewById(R.id.imgLearned);
        }
    }
}