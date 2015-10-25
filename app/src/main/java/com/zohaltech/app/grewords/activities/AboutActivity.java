package com.zohaltech.app.grewords.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zohaltech.app.grewords.BuildConfig;
import com.zohaltech.app.grewords.classes.App;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.classes.Helper;

import widgets.MySnackbar;
import widgets.MyToast;


public class AboutActivity extends EnhancedActivity {

    TextView     txtVersion;
    Button       btnShare;
    Button       btnProducts;
    Button       btnFeedback;
    Button       btnRate;
    LinearLayout layoutWebsite;

    @Override
    protected void onCreated() {
        setContentView(R.layout.activity_about);

        txtVersion = (TextView) findViewById(R.id.txtVersion);
        btnShare = (Button) findViewById(R.id.btnShare);
        btnProducts = (Button) findViewById(R.id.btnProducts);
        btnFeedback = (Button) findViewById(R.id.btnFeedback);
        btnRate = (Button) findViewById(R.id.btnRate);
        layoutWebsite = (LinearLayout) findViewById(R.id.layoutWebsite);

        txtVersion.setText("Version " + BuildConfig.VERSION_NAME);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String message = String.format(getResources().getString(R.string.sharing_message),
                                               getResources().getString(R.string.app_name),
                                               App.marketWebsiteUri);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_title)));
            }
        });

        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(App.marketDeveloperUri));
                if (!myStartActivity(intent)) {
                    MySnackbar.show(layoutWebsite, getString(R.string.could_not_open_market), Snackbar.LENGTH_SHORT);
                }
            }
        });

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "info@zohaltech.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.feedback_subject));
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.feedback_title)));
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.marketPollIntent);
                intent.setData(Uri.parse(App.marketPollUri));
                intent.setPackage(App.marketPackage);
                if (!myStartActivity(intent)) {
                    intent.setData(Uri.parse(App.marketWebsiteUri));
                    if (!myStartActivity(intent)) {
                        MyToast.show(String.format(getResources().getString(R.string.could_not_open_market), App.marketName, App.marketName), Toast.LENGTH_SHORT);
                    }
                }
            }
        });

        layoutWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.goToWebsite("http://zohaltech.com");
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
