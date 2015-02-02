package com.dolphin.utils;


import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class ConnectionUtils {

    @SuppressWarnings("unused")
    private static final String TAG = Log.getNormalizedTag(ConnectionUtils.class);
    private static final String HTTPS_PROTOCOL = "https";

    protected static URL sServerUrl;

    protected static void configureHttpsUrlConnection() {

        try {
            SSLContext sslContext = SSLContext.getDefault();

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new BrowserCompatHostnameVerifier());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e);
            throw new RuntimeException();
        }
    }

    public static void init(String url) {
        try {
            sServerUrl = new URL(url);
            if (HTTPS_PROTOCOL.equals(sServerUrl.getProtocol()))
                configureHttpsUrlConnection();
        }
        catch (MalformedURLException e) {
            Log.e("Selected server url", sServerUrl.toString(), e);
        }
    }

    public static String getHttpScheme() {
        return sServerUrl.getProtocol();
    }

    public static String getHostUrl() {
        return sServerUrl.getAuthority();
    }
}
