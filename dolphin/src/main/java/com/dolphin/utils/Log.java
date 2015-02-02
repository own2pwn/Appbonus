package com.dolphin.utils;

import com.dolphin.exceptionhandler.ExceptionHandler;

public class Log {
    private static ExceptionHandler handler;

    public static String getNormalizedTag(Class aClass) {
        return aClass != null ? aClass.getSimpleName() : "";
    }

    public static void e(String tag, Throwable e) {
        if (handler != null)
            handler.error(e, tag);
        android.util.Log.e(tag, e.getMessage());
    }

    public static void e(String tag, String message, Throwable e) {
        if (handler != null)
            handler.error(e, tag + "-" + message);
        Log.e(tag + "-" + message, e);
    }

    public static void d(String tag, String message) {
        android.util.Log.d(tag, message);
    }

    public static void setExceptionHandler(ExceptionHandler exceptionHandler) {
        handler = exceptionHandler;
    }
}
