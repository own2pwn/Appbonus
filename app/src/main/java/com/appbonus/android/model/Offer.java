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
    protected boolean done;
    protected String downloadLink;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
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
        dest.writeByte(done ? (byte) 1 : (byte) 0);
        dest.writeString(this.downloadLink);
    }

    public Offer() {
    }

    private Offer(Parcel in) {
        this.description = in.readString();
        this.icon = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.reward = (Double) in.readValue(Double.class.getClassLoader());
        this.title = in.readString();
        this.done = in.readByte() != 0;
        this.downloadLink = in.readString();
    }

    public static final Parcelable.Creator<Offer> CREATOR = new Parcelable.Creator<Offer>() {
        public Offer createFromParcel(Parcel source) {
            return new Offer(source);
        }

        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };
}
