package com.appbonus.android.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class Storage {
    static String tag;

    public static void init(Context context) {
        tag = context.getPackageName();
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        Map<String, ?> all = preferences.getAll();
        return (T) all.get(key);
    }

    public static <T> void save(Context context, String key, T obj) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        } else if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof String) {
            editor.putString(key, (String) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        }
        editor.apply();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(tag, Context.MODE_PRIVATE);
    }
}
