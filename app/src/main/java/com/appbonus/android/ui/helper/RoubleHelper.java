package com.appbonus.android.ui.helper;

public class RoubleHelper {
    public static String convert(Object object) {
        String pattern = "%sa";
        if (object instanceof Number) {
            return String.format(pattern, ((Number) object).intValue());
        }
        return String.format(pattern, object);
    }
}
