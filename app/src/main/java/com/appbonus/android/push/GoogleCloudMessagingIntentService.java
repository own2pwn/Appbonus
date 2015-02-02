/*
 * Copyright (c) 2014 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.appbonus.android.push;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.appbonus.android.R;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.appbonus.android.ui.MainActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GoogleCloudMessagingIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private List<OnPushNotificationReceiveListener> listeners = new CopyOnWriteArrayList<OnPushNotificationReceiveListener>();

    private Handler mHandler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int onStartCommand = super.onStartCommand(intent, flags, startId);
        mHandler = new Handler(getMainLooper());
        return onStartCommand;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        initHandlers(this);
    }

    public GoogleCloudMessagingIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                processNotification(extras);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GoogleCloudMessagingBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void processNotification(Bundle extras) {
        for (OnPushNotificationReceiveListener listener : listeners) {
            listener.onPushReceive(extras);
        }
    }

    private boolean addListener(OnPushNotificationReceiveListener listener) {
        return listeners.add(listener);
    }

    private boolean removeListener(OnPushNotificationReceiveListener listener) {
        return listeners.remove(listener);
    }

    public void initHandlers(final Context context) {
        addListener(new OnPushNotificationReceiveListener() {
            @Override
            public boolean onPushReceive(Bundle extras) {
                if (SharedPreferencesStorage.getPushShowing(context)) {
                    sendNotification(extras);
                }
                return false;
            }
        });
    }

    private void sendNotification(Bundle bundle) {
        PushNotification pushNotification = new PushNotification();

        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class).putExtra("push", pushNotification),
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_bonus_icon)
                        .setContentTitle(getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(pushNotification.getMessage()))
                        .setContentText(pushNotification.getMessage());

        mBuilder.setContentIntent(contentIntent);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }
}
