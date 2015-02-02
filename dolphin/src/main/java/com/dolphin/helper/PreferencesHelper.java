package com.dolphin.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.dolphin.json.JsonHandler;

import java.util.List;
import java.util.UUID;


public abstract class PreferencesHelper {
    protected Context context;

    protected PreferencesHelper(Context context) {
        this.context = context;
    }

    protected <T> T load(String key, Class<T> aClass) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(key, "");
        JsonHandler<T> jsonHandler = new JsonHandler<T>(aClass);
        return jsonHandler.fromJsonString(string);
    }

    protected <T> void save(String key, T entity, Class<T> aClass) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JsonHandler<T> jsonHandler = new JsonHandler<T>(aClass);
        editor.putString(key, jsonHandler.toJsonString(entity));
        editor.apply();
    }

    protected String get(String key, String defaultString) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultString);
    }

    protected int get(String key, int defaultInt) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultInt);
    }

    protected double get(String key, double defaultDouble) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, (float) defaultDouble);
    }

    protected long get(String key, long defaultLong) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultLong);
    }

    protected boolean get(String key, boolean defaultBool) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultBool);
    }

    protected void set(String key, String string) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, string);
        editor.apply();
    }

    protected void set(String key, boolean b) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, b);
        editor.apply();
    }

    protected void set(String key, int number) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, number);
        editor.apply();
    }

    protected void set(String key, double number) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, (float) number);
        editor.apply();
    }

    protected void set(String key, long number) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, number);
        editor.apply();
    }

    protected String tag() {
        return context != null ? context.getPackageName() : UUID.randomUUID().toString();
    }

    protected void clear(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, "");
        editor.apply();
    }

    public <T> void saveList(String key, List<T> list, Class<T> tClass) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JsonHandler<T> jsonHandler = new JsonHandler<T>(tClass);
        editor.putString(key, jsonHandler.listToJsonString(list));
        editor.apply();
    }

    public <T> List<T> loadList(String key, Class<T> tClass) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(key, "");
        JsonHandler<T> jsonHandler = new JsonHandler<T>(tClass);
        return jsonHandler.listFromJsonString(string);
    }

    public SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(tag(), Context.MODE_PRIVATE);
    }
}
