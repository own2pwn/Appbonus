package com.appbonus.android.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.appbonus.android.R;

public class BannedDeviceActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banned_device);

        TextView textView = (TextView) findViewById(android.R.id.text1);
        textView.setText(getIntent().getIntExtra("message", 0));
    }
}
