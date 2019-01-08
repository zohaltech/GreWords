package com.zohaltech.app.grewords.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.zohaltech.app.grewords.BuildConfig;
import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.classes.App;
import com.zohaltech.app.grewords.classes.Helper;

import java.text.MessageFormat;

import widgets.MySnackbar;


public class AboutActivity extends EnhancedActivity {
    
    TextView           txtVersion;
    Button             btnShare;
    Button             btnProducts;
    Button             btnFeedback;
    Button             btnRate;
    LinearLayoutCompat layoutWebsite;
    
    @Override
    protected void onCreated() {
        setContentView(R.layout.activity_about);
        
        txtVersion = findViewById(R.id.txtVersion);
        btnShare = findViewById(R.id.btnShare);
        btnProducts = findViewById(R.id.btnProducts);
        btnFeedback = findViewById(R.id.btnFeedback);
        btnRate = findViewById(R.id.btnRate);
        layoutWebsite = findViewById(R.id.layoutWebsite);
        
        txtVersion.setText(MessageFormat.format("Version {0}", BuildConfig.VERSION_NAME));
        
        Drawable drawable1 = ContextCompat.getDrawable(this, R.drawable.share);
        btnShare.setCompoundDrawablesWithIntrinsicBounds(null, drawable1, null, null);
        btnShare.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String message = String.format(getResources().getString(R.string.sharing_message),
                    getResources().getString(R.string.app_name),
                    App.marketWebsiteUri);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_title)));
        });
        
        Drawable drawable2 = ContextCompat.getDrawable(this, R.drawable.products);
        btnProducts.setCompoundDrawablesWithIntrinsicBounds(null, drawable2, null, null);
        btnProducts.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(App.marketDeveloperUri));
            if (!myStartActivity(intent)) {
                MySnackbar.show(layoutWebsite, String.format(getString(R.string.could_not_open_market), App.marketName), Snackbar.LENGTH_SHORT);
            }
        });
        
        Drawable drawable3 = ContextCompat.getDrawable(this, R.drawable.share);
        btnFeedback.setCompoundDrawablesWithIntrinsicBounds(null, drawable3, null, null);
        btnFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "zohaltech@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.feedback_subject));
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.feedback_title)));
        });
        
        Drawable drawable4 = ContextCompat.getDrawable(this, R.drawable.rate);
        btnRate.setCompoundDrawablesWithIntrinsicBounds(null, drawable4, null, null);
        btnRate.setOnClickListener(v -> Helper.rateApp(AboutActivity.this));
        
        layoutWebsite.setOnClickListener(v -> {
            if (App.market == App.MARKET_PLAY) {
                Helper.goToWebsite(this, "http://zohaltech.com/en/index.html");
            } else {
                Helper.goToWebsite(this, "http://zohaltech.com");
            }
        });
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
            actionBar.setTitle("About");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
    
    private boolean myStartActivity(Intent intent) {
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
