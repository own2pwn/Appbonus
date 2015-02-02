package com.dolphin.utils;


import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * Created at 14.01.14 18:59
 *
 * @author Altero
 */
public class ConnectionUtilsInsecure extends ConnectionUtils {

    @SuppressWarnings("unused")
    private static final String TAG = Log.getNormalizedTag(ConnectionUtilsInsecure.class);

    protected static void configureHttpsUrlConnection() {

        try {

            TrustManager trustManager = new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {

                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {

                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {

                }
            };

            TrustManager[] trustManagers = new TrustManager[] {
                    trustManager
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new AllowAllHostnameVerifier());
        }
        catch (KeyManagementException e) {
            Log.e(TAG, e);
            throw new RuntimeException();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e);
            throw new RuntimeException();
        }
    }
}
