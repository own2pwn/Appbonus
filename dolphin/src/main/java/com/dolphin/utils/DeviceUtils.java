package com.dolphin.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by darkdelphin on 27.08.2014.
 */
public class DeviceUtils {
    public static String getUniqueCode(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        return wifiInf.getMacAddress();
    }
}
