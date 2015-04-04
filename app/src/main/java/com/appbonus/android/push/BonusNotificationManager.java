package com.appbonus.android.push;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.appbonus.android.R;
import com.appbonus.android.ui.MainActivity;
import com.dolphin.push.AbstractNotificationManager;
import com.dolphin.push.Notification;

public class BonusNotificationManager extends AbstractNotificationManager {
    public BonusNotificationManager(Context context) {
        super(context);
    }

    @Override
    protected int icon() {
        return R.drawable.app_bonus_icon;
    }

    @Override
    protected int appName() {
        return R.string.app_name;
    }

    @Override
    protected Class target() {
        return MainActivity.class;
    }

    @Override
    protected NotificationCompat.Builder notificationBuilder(Notification notification) {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(icon()) //иконка уведомления
                .setAutoCancel(true) //уведомление закроется по клику на него
                .setTicker(notification.message()) //текст, который отобразится вверху статус-бара при создании уведомления
                .setContentText(notification.message()) // Основной текст уведомления
                .setWhen(System.currentTimeMillis()) //отображаемое время уведомления
                .setContentTitle(context.getString(appName())) //заголовок уведомления
                .setDefaults(android.app.Notification.DEFAULT_LIGHTS | android.app.Notification.DEFAULT_VIBRATE) // вибро и диодный индикатор выставляются по умолчанию
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification_sound))
                .setNumber(++numMessages);
    }
}
