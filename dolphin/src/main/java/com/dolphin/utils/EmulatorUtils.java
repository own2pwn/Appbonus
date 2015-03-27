package com.dolphin.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

public class EmulatorUtils {
    public static boolean isEmulator(Context context) {
        if (Build.PRODUCT.matches(".*_?sdk_?.*")) {
            return true;
        } else if (Build.FINGERPRINT.startsWith("generic")) {
            return true;
        } else if ("goldfish".equalsIgnoreCase(Build.HARDWARE)) {
            return true;
        } else {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null && "android".equalsIgnoreCase(tm.getNetworkOperatorName())) {
                return true;
            }
        }
        return false;
    }
}
