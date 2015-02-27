package com.appbonus.android.model.api;

import android.os.Parcel;
import android.os.Parcelable;

public class DoneOffersWrapper implements Parcelable {
    protected int[] doneIds;

    public int[] getDoneIds() {
        return doneIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(this.doneIds);
    }

    public DoneOffersWrapper() {
    }

    private DoneOffersWrapper(Parcel in) {
        in.readIntArray(this.doneIds);
    }

    public static final Parcelable.Creator<DoneOffersWrapper> CREATOR = new Parcelable.Creator<DoneOffersWrapper>() {
        public DoneOffersWrapper createFromParcel(Parcel source) {
            return new DoneOffersWrapper(source);
        }

        public DoneOffersWrapper[] newArray(int size) {
            return new DoneOffersWrapper[size];
        }
    };
}
