package com.appbonus.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Notification implements com.dolphin.push.Notification, Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.notificationType);
        dest.writeParcelable(this.payload, flags);
        dest.writeString(this.message);
    }

    public Notification() {
    }

    private Notification(Parcel in) {
        this.notificationType = in.readString();
        this.payload = in.readParcelable(Payload.class.getClassLoader());
        this.message = in.readString();
    }

    public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
