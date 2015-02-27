package com.appbonus.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class AuthService implements Serializable, Parcelable {
    protected long id;
    protected long userId;
    protected String provider;
    protected String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.userId);
        dest.writeString(this.provider);
        dest.writeString(this.url);
    }

    public AuthService() {
    }

    private AuthService(Parcel in) {
        this.id = in.readLong();
        this.userId = in.readLong();
        this.provider = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<AuthService> CREATOR = new Parcelable.Creator<AuthService>() {
        public AuthService createFromParcel(Parcel source) {
            return new AuthService(source);
        }

        public AuthService[] newArray(int size) {
            return new AuthService[size];
        }
    };
}
