package com.appbonus.android.model.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.appbonus.android.model.User;

import java.io.Serializable;

public class LoginWrapper implements Serializable, Parcelable {
    protected User user;
    protected String authToken;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, 0);
        dest.writeString(this.authToken);
    }

    public LoginWrapper() {
    }

    private LoginWrapper(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
        this.authToken = in.readString();
    }

    public static final Parcelable.Creator<LoginWrapper> CREATOR = new Parcelable.Creator<LoginWrapper>() {
        public LoginWrapper createFromParcel(Parcel source) {
            return new LoginWrapper(source);
        }

        public LoginWrapper[] newArray(int size) {
            return new LoginWrapper[size];
        }
    };
}
