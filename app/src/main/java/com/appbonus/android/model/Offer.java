package com.appbonus.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Offer implements Serializable, Parcelable {
    protected String description;
    protected String icon;
    protected Long id;
    protected Double reward;
    protected String title;
    protected String downloadLink;
    protected boolean completed = false;

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public Long getId() {
        return id;
    }

    public Double getReward() {
        return reward;
    }

    public String getTitle() {
        return title;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Offer() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.icon);
        dest.writeValue(this.id);
        dest.writeValue(this.reward);
        dest.writeString(this.title);
        dest.writeString(this.downloadLink);
        dest.writeByte(completed ? (byte) 1 : (byte) 0);
    }

    private Offer(Parcel in) {
        this.description = in.readString();
        this.icon = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.reward = (Double) in.readValue(Double.class.getClassLoader());
        this.title = in.readString();
        this.downloadLink = in.readString();
        this.completed = in.readByte() != 0;
    }

    public static final Creator<Offer> CREATOR = new Creator<Offer>() {
        public Offer createFromParcel(Parcel source) {
            return new Offer(source);
        }

        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };
}
