package com.appbonus.android.push;

import android.os.Bundle;

import com.dolphin.push.GoogleCloudMessagingIntentService;

public class BonusGCMService extends GoogleCloudMessagingIntentService {
    @Override
    protected void processNotification(Bundle extras) {
        //todo
        //Вам начислено 30р. за Clash of Clans
        String alert = extras.getString("alert");
        //{"notification_type":"balance","payload":{"referral_level":null,"created_at":"2015-02-12T15:16:39.403+03:00","operation_type":"installation"}}
        String data = extras.getString("custom_data");
    }
}
