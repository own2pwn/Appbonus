package com.appbonus.android.push;

import android.content.Context;

import com.appbonus.android.R;
import com.appbonus.android.ui.MainActivity;
import com.dolphin.push.AbstractNotificationManager;

public class BonusNotificationManager extends AbstractNotificationManager {
    public BonusNotificationManager(Context context) {
        super(context);
    }

    @Override
    protected int icon() {
        return R.drawable.ic_launcher;
    }

    @Override
    protected int appName() {
        return R.string.app_name;
    }

    @Override
    protected Class target() {
        return MainActivity.class;
    }
}
