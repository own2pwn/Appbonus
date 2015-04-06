package com.appbonus.android.model.api;

import android.os.Parcel;
import android.os.Parcelable;

public class DoneOffersWrapper implements Parcelable {
    protected int[] doneIds;
    protected int[] installedIds;

    public int[] getDoneIds() {
        return doneIds;
    }

    public int[] getInstalledIds() {
        return installedIds;
    }

    public DoneOffersWrapper() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(this.doneIds);
        dest.writeIntArray(this.installedIds);
    }

    private DoneOffersWrapper(Parcel in) {
        this.doneIds = in.createIntArray();
        this.installedIds = in.createIntArray();
    }

    public static final Creator<DoneOffersWrapper> CREATOR = new Creator<DoneOffersWrapper>() {
        public DoneOffersWrapper createFromParcel(Parcel source) {
            return new DoneOffersWrapper(source);
        }

        public DoneOffersWrapper[] newArray(int size) {
            return new DoneOffersWrapper[size];
        }
    };
}
