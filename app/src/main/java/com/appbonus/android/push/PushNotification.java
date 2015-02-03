package com.appbonus.android.push;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author kudrenko
 * @version $Id$
 */
public class PushNotification implements Parcelable {
    protected String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
    }

    public PushNotification() {
    }

    private PushNotification(Parcel in) {
        message = in.readString();
    }

    public static Creator<PushNotification> CREATOR = new Creator<PushNotification>() {
        public PushNotification createFromParcel(Parcel source) {
            return new PushNotification(source);
        }

        public PushNotification[] newArray(int size) {
            return new PushNotification[size];
        }
    };
}
