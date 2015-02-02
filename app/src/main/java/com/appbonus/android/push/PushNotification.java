package com.appbonus.android.push;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author kudrenko
 * @version $Id$
 */
public class PushNotification implements Parcelable {
    private String message;
    private int badge;
    private int postId;
    private String type;

    public String getMessage() {
        return message;
    }

    public int getBadge() {
        return badge;
    }

    public int getPostId() {
        return postId;
    }

    public String getType() {
        return type;
    }

    public boolean isBest() {
        return "best".equals(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeInt(this.badge);
        dest.writeInt(this.postId);
        dest.writeString(this.type);
    }

    public PushNotification() {
    }

    private PushNotification(Parcel in) {
        this.message = in.readString();
        this.badge = in.readInt();
        this.postId = in.readInt();
        this.type = in.readString();
    }

    public static Creator<PushNotification> CREATOR = new Creator<PushNotification>() {
        public PushNotification createFromParcel(Parcel source) {
            return new PushNotification(source);
        }

        public PushNotification[] newArray(int size) {
            return new PushNotification[size];
        }
    };

    public PushNotification(String message, int badge, int postId, String type) {
        this.message = message;
        this.badge = badge;
        this.postId = postId;
        this.type = type;
    }
}
