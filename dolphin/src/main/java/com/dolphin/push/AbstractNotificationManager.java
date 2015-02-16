package com.dolphin.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public abstract class AbstractNotificationManager {
    private static final String LOG_TAG = AbstractNotificationManager.class.getSimpleName();

    protected Context context;
    protected PowerManager powerManager;
    protected NotificationManager notificationManager;

    private int numMessages = 0;

    public AbstractNotificationManager(Context context) {
        this.context = context;
        this.powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void createInfoNotification(com.dolphin.push.Notification notification) {
        PowerManager.WakeLock screenLock = powerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, LOG_TAG);

        Class target = target();
        Intent notificationIntent = new Intent(context, target).putExtra("notification", notification);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(target);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(notificationIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(icon()) //иконка уведомления
                .setAutoCancel(true) //уведомление закроется по клику на него
                .setTicker(notification.message()) //текст, который отобразится вверху статус-бара при создании уведомления
                .setContentText(notification.message()) // Основной текст уведомления
                .setWhen(System.currentTimeMillis()) //отображаемое время уведомления
                .setContentTitle(context.getString(appName())) //заголовок уведомления
                .setDefaults(Notification.DEFAULT_ALL) // звук, вибро и диодный индикатор выставляются по умолчанию
                .setNumber(++numMessages);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(0, builder.build());

        screenLock.acquire();
    }

    protected abstract int icon();
    protected abstract int appName();
    protected abstract Class target();
}
