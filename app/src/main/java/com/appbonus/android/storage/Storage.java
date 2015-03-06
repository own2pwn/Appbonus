package com.appbonus.android.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.dolphin.json.JsonHandler;

import java.util.Map;

public class Storage {
    static String tag;

    public static void init(Context context) {
        tag = context.getPackageName();
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(Context context, String key) {
        SharedPreferences preferences = getPreferences(context);
        Map<String, ?> all = preferences.getAll();
        return (T) all.get(key);
    }

    public static <T> T load(Context context, String key, Class<T> tClass) {
        String string = load(context, key);
        JsonHandler<T> jsonHandler = new JsonHandler<>(tClass);
        return jsonHandler.fromJsonString(string);
    }

    public static <T> void save(Context context, String key, T obj) {
        SharedPreferences preferences = getPreferences(context);
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
        } else {
            Class<T> tClass = (Class<T>) obj.getClass();
            JsonHandler<T> jsonHandler = new JsonHandler<>(tClass);
            editor.putString(key, jsonHandler.toJsonString(obj));
        }
        editor.apply();
    }

    public static void delete(Context context, String key) {
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(tag, Context.MODE_PRIVATE);
    }
}
