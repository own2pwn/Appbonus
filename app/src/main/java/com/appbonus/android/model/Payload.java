package com.appbonus.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Payload implements Parcelable {
    protected Integer referralLevel;
    protected Date createdDate;
    protected String operationType;

    public Integer getReferralLevel() {
        return referralLevel;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getOperationType() {
        return operationType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.referralLevel);
        dest.writeLong(createdDate != null ? createdDate.getTime() : -1);
        dest.writeString(this.operationType);
    }

    public Payload() {
    }

    private Payload(Parcel in) {
        this.referralLevel = (Integer) in.readValue(Integer.class.getClassLoader());
        long tmpCreatedDate = in.readLong();
        this.createdDate = tmpCreatedDate == -1 ? null : new Date(tmpCreatedDate);
        this.operationType = in.readString();
    }

    public static final Parcelable.Creator<Payload> CREATOR = new Parcelable.Creator<Payload>() {
        public Payload createFromParcel(Parcel source) {
            return new Payload(source);
        }

        public Payload[] newArray(int size) {
            return new Payload[size];
        }
    };
}
