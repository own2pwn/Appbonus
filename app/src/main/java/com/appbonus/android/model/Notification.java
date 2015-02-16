package com.appbonus.android.model;

public class Notification implements com.dolphin.push.Notification {
    public static final String BALANCE = "balance";
    public static final String REFERRALS = "referrals";

    protected String notificationType;
    protected Payload payload;
    protected String message;

    public String getNotificationType() {
        return notificationType;
    }

    public Payload getPayload() {
        return payload;
    }

    @Override
    public String message() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Notification createRandom() {
        Notification notification = new Notification();
        notification.message = "Random message";
        notification.notificationType = BALANCE;
        return notification;
    }
}
