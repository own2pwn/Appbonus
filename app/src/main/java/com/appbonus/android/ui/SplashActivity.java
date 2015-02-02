package com.appbonus.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.appbonus.android.R;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.appbonus.android.ui.helper.IntentHelper;
import com.appbonus.android.ui.login.LoginActivity;

public class SplashActivity extends Activity {
    public static final long MILLS_PER_SECOND = 1000;
    public static final long SPLASH_DELAY = MILLS_PER_SECOND * 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(SharedPreferencesStorage.getToken(SplashActivity.this))) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else startActivity(IntentHelper.openMain(SplashActivity.this));
                finish();
            }
        }, SPLASH_DELAY);
    }
}
