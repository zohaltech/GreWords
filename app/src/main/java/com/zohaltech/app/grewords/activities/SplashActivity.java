package com.zohaltech.app.grewords.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.classes.App;

public class SplashActivity extends AppCompatActivity {

    ImageView imgSplashBorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgSplashBorder = findViewById(R.id.imgSplashBorder);

        App.handler.post(() -> {
            Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.rotate);
            imgSplashBorder.startAnimation(animation);
        });

        App.handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        //nothing
    }
}
