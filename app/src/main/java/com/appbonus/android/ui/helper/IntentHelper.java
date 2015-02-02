package com.appbonus.android.ui.helper;

import android.content.Context;
import android.content.Intent;

import com.appbonus.android.ui.MainActivity;

public class IntentHelper {
    public static Intent openMain(Context context) {
        return new Intent(context, MainActivity.class);
    }
}
