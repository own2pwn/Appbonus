package com.dolphin.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.HttpCookie;
import java.net.URI;
import java.util.List;


public class NetUtils {

    public static class CookieStore implements java.net.CookieStore {

        @SuppressWarnings("unused")
        private static final String TAG = Log.getNormalizedTag(CookieStore.class);

        @Override
        public void add(URI uri, HttpCookie cookie) {

            Log.d(TAG, "Add");
        }

        @Override
        public List<HttpCookie> get(URI uri) {

            return null;
        }

        @Override
        public List<HttpCookie> getCookies() {

            return null;
        }

        @Override
        public List<URI> getURIs() {

            return null;
        }

        @Override
        public boolean remove(URI uri, HttpCookie cookie) {

            return false;
        }

        @Override
        public boolean removeAll() {

            return false;
        }
    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

}
