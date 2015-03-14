package com.appbonus.android.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.appbonus.android.R;

public class RootedDeviceActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooted_device);
    }
}
