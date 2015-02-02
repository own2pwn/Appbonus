package com.dolphin.activity.fragment;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

public class ExtPoint extends Point implements Parcelable {
    public ExtPoint() {
        super();
    }

    public ExtPoint(int x, int y) {
        super(x, y);
    }

    /**
     * Parcelable interface methods
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write this point to the specified parcel. To restore a point from
     * a parcel, use readFromParcel()
     * @param out The parcel to write the point's coordinates into
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(x);
        out.writeInt(y);
    }

    public static final Creator<ExtPoint> CREATOR = new Creator<ExtPoint>() {
        /**
         * Return a new point from the data in the specified parcel.
         */
        public ExtPoint createFromParcel(Parcel in) {
            ExtPoint r = new ExtPoint();
            r.readFromParcel(in);
            return r;
        }

        /**
         * Return an array of rectangles of the specified size.
         */
        public ExtPoint[] newArray(int size) {
            return new ExtPoint[size];
        }
    };

    /**
     * Set the point's coordinates from the data stored in the specified
     * parcel. To write a point to a parcel, call writeToParcel().
     *
     * @param in The parcel to read the point's coordinates from
     */
    public void readFromParcel(Parcel in) {
        x = in.readInt();
        y = in.readInt();
    }
}
