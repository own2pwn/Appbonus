package com.appbonus.android.push;

import com.dolphin.push.GoogleCloudMessagingBroadcastReceiver;

public class BonusGCMReceiver extends GoogleCloudMessagingBroadcastReceiver {
    @Override
    protected String serviceName() {
        return BonusGCMService.class.getName();
    }
}
