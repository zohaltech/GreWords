package com.zohaltech.app.grewords.activities;


import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.adapters.VocabularyAdapter;
import com.zohaltech.app.grewords.data.Vocabularies;
import com.zohaltech.app.grewords.entities.Vocabulary;

import java.util.ArrayList;

public class BookmarksActivity extends EnhancedActivity {

    RecyclerView recyclerBookmarks;
    TextView     txtNothingFound;
    ArrayList<Vocabulary> vocabularies = new ArrayList<>();
    VocabularyAdapter adapter;

    @Override
    protected void onCreated() {
        setContentView(R.layout.activity_bookmarks);

        recyclerBookmarks = findViewById(R.id.recyclerBookmarks);
        txtNothingFound = findViewById(R.id.txtNothingFound);
        recyclerBookmarks.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerBookmarks.setLayoutManager(layoutManager);
        
        //vocabularies = Vocabularies.selectBookmarks();
        adapter = new VocabularyAdapter(this, vocabularies);
        recyclerBookmarks.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adapter.getItemCount() > 0) {
                    txtNothingFound.setVisibility(View.GONE);
                } else {
                    txtNothingFound.setVisibility(View.VISIBLE);
                }
            }
        });

        //adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vocabularies.clear();
        vocabularies.addAll(Vocabularies.selectBookmarks(this));
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onToolbarCreated() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Bookmarks");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
}
