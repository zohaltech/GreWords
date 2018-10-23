package com.zohaltech.app.grewords.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zohaltech.app.grewords.R;

public abstract class EnhancedActivity extends AppCompatActivity {

    Toolbar toolbar;
    //TextView txtToolbarTitle;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);

        onCreated();
        onInitialized();
        onToolbarCreated();
    }

    private void onInitialized() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    protected abstract void onCreated();

    protected abstract void onToolbarCreated();
}
