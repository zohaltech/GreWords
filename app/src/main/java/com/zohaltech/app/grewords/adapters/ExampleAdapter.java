package com.zohaltech.app.grewords.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zohaltech.app.grewords.entities.Example;

import java.util.ArrayList;

import com.zohaltech.app.grewords.R;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ViewHolder> {
    Context            context;
    ArrayList<Example> examples;

    public ExampleAdapter(Context context, ArrayList<Example> examples) {
        this.context = context;
        this.examples = examples;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate( R.layout.adapter_example, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Example example = examples.get(position);
        holder.txtExample.setText(example.getSynonyms());
        holder.txtOpposites.setText(example.getOpposites());
    }

    @Override
    public int getItemCount() {
        return examples.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtExample;
        public TextView txtOpposites;

        public ViewHolder(View view) {
            super(view);
            txtExample = view.findViewById(R.id.txtExample);
            txtOpposites = view.findViewById(R.id.txtOpposites);
        }
    }
}
