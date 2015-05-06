package com.appbonus.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Settings implements Parcelable {
    private double partnerSignUpBonus;

    public double getPartnerSignUpBonus() {
        return partnerSignUpBonus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.partnerSignUpBonus);
    }

    public Settings() {
    }

    private Settings(Parcel in) {
        this.partnerSignUpBonus = in.readDouble();
    }

    public static final Parcelable.Creator<Settings> CREATOR = new Parcelable.Creator<Settings>() {
        public Settings createFromParcel(Parcel source) {
            return new Settings(source);
        }

        public Settings[] newArray(int size) {
            return new Settings[size];
        }
    };
}
