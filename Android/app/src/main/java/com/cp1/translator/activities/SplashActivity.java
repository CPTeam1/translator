package com.cp1.translator.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.cp1.translator.R;
import com.cp1.translator.login.LoginActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView ivAppIcon = (ImageView) findViewById(R.id.ivAppIcon);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(SplashActivity.this, (View)ivAppIcon, "appIcon");
                startActivity(intent, options.toBundle());

                // close this Activity
//                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
