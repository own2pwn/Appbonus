package com.dolphin.helper;


import android.content.Context;
import android.net.Uri;

public final class RatingAppHelper {
    private static final String PLAY_GOOGLE_APP_PATH = "https://play.google.com/store/apps/details?id=%s";

    private static final String RATING_KEY = "rating_key";
    private static final int UNKNOWN_COUNT = -1;
    private static final int STOP_COUNT = -2;

    private static final int DEFAULT_COUNT = 1;
    private static final int DEFAULT_FACTOR = 2;

    private int count = DEFAULT_COUNT;
    private int factor = DEFAULT_FACTOR;
    private Context mContext;

    public RatingAppHelper(Context mContext, int count, int factor) {
        this.mContext = mContext;
        this.factor = factor;
        this.count = count;
    }

    public boolean checkRating() {
        RatingPreferencesHelper helper = new RatingPreferencesHelper(mContext);
        int current = helper.get(RATING_KEY, UNKNOWN_COUNT);
        if (count == current) {
            return true;
        }
        helper.set(RATING_KEY, ++current);
        return false;
    }

    public void skip() {
        RatingPreferencesHelper helper = new RatingPreferencesHelper(mContext);
        int current = helper.get(RATING_KEY, UNKNOWN_COUNT);
        if (current != UNKNOWN_COUNT) {
            helper.set(RATING_KEY, current * factor);
        } else throw new RuntimeException("You cannot skip. Count is not initialized");
    }

    public String getGoogleAppPath() {
        return String.format(PLAY_GOOGLE_APP_PATH, mContext.getPackageName());
    }

    public Uri getGoogleAppUri() {
        return Uri.parse(getGoogleAppPath());
    }

    public void stop() {
        RatingPreferencesHelper helper = new RatingPreferencesHelper(mContext);
        helper.set(RATING_KEY, STOP_COUNT);
    }

    private class RatingPreferencesHelper extends PreferencesHelper {
        protected RatingPreferencesHelper(Context context) {
            super(context);
        }
    }
}
