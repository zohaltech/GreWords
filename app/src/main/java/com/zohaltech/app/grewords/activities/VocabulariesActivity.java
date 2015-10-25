package com.zohaltech.app.grewords.activities;


import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.adapters.VocabularyAdapter;
import com.zohaltech.app.grewords.data.Vocabularies;
import com.zohaltech.app.grewords.entities.Vocabulary;

import java.util.ArrayList;

public class VocabulariesActivity extends EnhancedActivity {

    public static final String LESSON = "LESSON";
    private int lesson;

    @Override
    protected void onCreated() {
        setContentView(R.layout.activity_vocabularies);
        RecyclerView recyclerVocabularies = (RecyclerView) findViewById(R.id.recyclerVocabularies);
        recyclerVocabularies.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerVocabularies.setLayoutManager(layoutManager);
        lesson = getIntent().getIntExtra(LESSON, 0);
        ArrayList<Vocabulary> vocabularies = Vocabularies.selectByLesson(lesson);
        VocabularyAdapter adapter = new VocabularyAdapter(this, vocabularies, true);
        recyclerVocabularies.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onToolbarCreated() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Lesson " + lesson);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
}
