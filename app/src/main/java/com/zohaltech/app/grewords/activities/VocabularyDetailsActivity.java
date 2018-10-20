package com.zohaltech.app.grewords.activities;


import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.adapters.DescriptionPagerAdapter;
import com.zohaltech.app.grewords.classes.App;
import com.zohaltech.app.grewords.data.Examples;
import com.zohaltech.app.grewords.data.Notes;
import com.zohaltech.app.grewords.data.Vocabularies;
import com.zohaltech.app.grewords.entities.Example;
import com.zohaltech.app.grewords.entities.Note;
import com.zohaltech.app.grewords.entities.Vocabulary;

import java.util.ArrayList;

import widgets.MySnackbar;

public class VocabularyDetailsActivity extends EnhancedActivity {
    public static final  String INIT_MODE_KEY = "INIT_MODE";
    //public static final String MODE_SEARCH_RESULT = "MODE_SEARCH_RESULT";
    public static final  String MODE_VIEW     = "MODE_VIEW";
    public static final  String VOCAB_ID      = "VOCAB_ID";
    //public static final String SEARCH_TEXT        = "SEARCH_TEXT";
    private static final int    TAB_COUNT     = 3;
    LinearLayout            layoutRoot;
    TextView                txtVocabulary;
    CheckBox                chkBookmark;
    PagerSlidingTabStrip    tabCategories;
    ViewPager               pagerCategories;
    DescriptionPagerAdapter descriptionPagerAdapter;
    ArrayList<Example>      examples;
    ArrayList<Note>         notes;
    Vocabulary              vocabulary;

    @Override
    protected void onCreated() {
        setContentView(R.layout.activity_vocabulary_details);

        layoutRoot = findViewById(R.id.layoutRoot);
        txtVocabulary = findViewById(R.id.txtVocabulary);
        chkBookmark = findViewById(R.id.chkBookmark);
        tabCategories = findViewById(R.id.tabDescriptions);
        pagerCategories = findViewById(R.id.pagerDescItems);

        int vocabularyId = getIntent().getIntExtra(VOCAB_ID, 0);

        vocabulary = Vocabularies.select(vocabularyId);
        assert vocabulary != null;
        txtVocabulary.setText(vocabulary.getVocabulary());

        chkBookmark.setChecked(vocabulary.getBookmarked());

        chkBookmark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean bookmarked = chkBookmark.isChecked();
            vocabulary.setBookmarked(bookmarked);
            if (Vocabularies.update(vocabulary) > 0) {
                if (bookmarked) {
                    MySnackbar.show(layoutRoot, "Vocabulary added to bookmarks", Snackbar.LENGTH_SHORT);
                } else {
                    MySnackbar.show(layoutRoot, "Vocabulary removed from bookmarks", Snackbar.LENGTH_SHORT);
                }
            }
        });

        examples = Examples.getExamples(vocabularyId);
        notes = Notes.getNotes(vocabularyId);

        ArrayList<String> tabTitles = new ArrayList<>();
        tabTitles.add("MEANING");
        tabTitles.add("SYN/OPP");
        tabTitles.add("NOTES");

        descriptionPagerAdapter = new DescriptionPagerAdapter(getSupportFragmentManager(), tabTitles, vocabularyId);
        pagerCategories.setAdapter(descriptionPagerAdapter);

        // Bind the tabCategories to the ViewPager
        tabCategories.setViewPager(pagerCategories);

        pagerCategories.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeTabTitleColors(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        changeTabsFont();
        changeTabTitleColors(0);
    }

    private void changeTabTitleColors(int position) {
        ((TextView) ((ViewGroup) tabCategories.getChildAt(0)).getChildAt(0)).setTextColor(getResources().getColor(R.color.secondary_text));
        ((TextView) ((ViewGroup) tabCategories.getChildAt(0)).getChildAt(1)).setTextColor(getResources().getColor(R.color.secondary_text));
        ((TextView) ((ViewGroup) tabCategories.getChildAt(0)).getChildAt(2)).setTextColor(getResources().getColor(R.color.secondary_text));
        ((TextView) ((ViewGroup) tabCategories.getChildAt(0)).getChildAt(position)).setTextColor(getResources().getColor(R.color.primary_text));
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
            actionBar.setTitle("Vocabulary Details");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabCategories.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            TextView textView = (TextView) vg.getChildAt(j);
            textView.setWidth(App.screenWidth / TAB_COUNT);
            //textView.setTypeface(App.englishFont);
            textView.setTextColor(getResources().getColor(R.color.secondary_text));
            textView.setTextSize(14);
        }
    }


}
