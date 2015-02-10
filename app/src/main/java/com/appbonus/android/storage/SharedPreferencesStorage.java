package com.appbonus.android.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.appbonus.android.model.User;
import com.dolphin.helper.PreferencesHelper;

public class SharedPreferencesStorage extends PreferencesHelper {
    private static SharedPreferencesStorage storage;

    private static final String TOKEN_PARAMETER = "token";
    private static final String PHONE_CONFIRMED = "phone_confirmed";
    private static final String BALANCE_PARAMETER = "balance";
    private static final String USER_ID = "user_id";
    private static final String AUTO_WITHDRAWAL_PARAMETER = "auto_withdrawal_parameter";
    private static final String QIWI_PARAMETER = "qiwi_parameter";
    private static final String MOBILE_PARAMETER = "mobile_parameter";
    private static final String USER_PARAMETER = "user";
    private static final String PASSWORD_PARAMETER = "password";

    protected SharedPreferencesStorage(Context context) {
        super(context);
    }

    public static void saveToken(Context context, String token) {
        SharedPreferencesStorage storage = getStorage(context);
        storage.set(TOKEN_PARAMETER, token);
    }

    public static String getToken(Context context) {
        SharedPreferencesStorage storage = getStorage(context);
        return storage.get(TOKEN_PARAMETER, "");
    }

    public static void deleteToken(Context context) {
        SharedPreferencesStorage storage = getStorage(context);
        storage.clear(TOKEN_PARAMETER);
    }

    public static boolean getPushShowing(Context context) {
        User user = getUser(context);
        return user.isNotify();
    }

    public static void confirmPhone(Context context, boolean confirmPhone) {
        SharedPreferencesStorage storage = getStorage(context);
        storage.set(PHONE_CONFIRMED, confirmPhone);
    }

    public static boolean isPhoneConfirmed(Context context) {
        SharedPreferencesStorage storage = getStorage(context);
        return storage.get(PHONE_CONFIRMED, false);
    }

    public static void saveBalance(Context context, double balance) {
        SharedPreferencesStorage storage = getStorage(context);
        storage.set(BALANCE_PARAMETER, balance);
    }

    public static double getBalance(Context context) {
        SharedPreferencesStorage storage = getStorage(context);
        return storage.get(BALANCE_PARAMETER, 0.0D);
    }

    public static void saveUserId(Context context, long userId) {
        SharedPreferencesStorage storage = getStorage(context);
        storage.set(USER_ID, userId);
    }

    public static long getUserId(Context context) {
        SharedPreferencesStorage storage = getStorage(context);
        return storage.get(USER_ID, 0L);
    }

    public static void setAutoWithdrawal(Context context, boolean autoWithdrawal) {
        SharedPreferencesStorage storage = getStorage(context);
        storage.set(AUTO_WITHDRAWAL_PARAMETER, autoWithdrawal);
    }

    public static boolean getAutoWithdrawal(Context context) {
        SharedPreferencesStorage storage = getStorage(context);
        return storage.get(AUTO_WITHDRAWAL_PARAMETER, false);
    }

    public static void saveMobile(Context context, String mobile) {
        SharedPreferencesStorage storage = getStorage(context);
        storage.set(MOBILE_PARAMETER, mobile);
    }

    public static String getMobile(Context context) {
        SharedPreferencesStorage storage = getStorage(context);
        return storage.get(MOBILE_PARAMETER, "");
    }

    public static void saveQiwi(Context context, String qiwi) {
        SharedPreferencesStorage storage = getStorage(context);
        storage.set(QIWI_PARAMETER, qiwi);
    }

    public static String getQiwi(Context context) {
        SharedPreferencesStorage storage = getStorage(context);
        return storage.get(QIWI_PARAMETER, "");
    }

    public static void savePassword(Context context, String password) {
        SharedPreferencesStorage storage = getStorage(context);
        storage.set(PASSWORD_PARAMETER, password);
    }

    public static String getPassword(Context context) {
        SharedPreferencesStorage storage = getStorage(context);
        return storage.get(PASSWORD_PARAMETER, "");
    }

    @SuppressWarnings("unchecked")
    public static void saveUser(Context context, User user) {
        SharedPreferencesStorage storage = getStorage(context);
        storage.save(USER_PARAMETER, user, (Class<User>) user.getClass());
    }

    public static User getUser(Context context) {
        SharedPreferencesStorage storage = getStorage(context);
        return storage.load(USER_PARAMETER, User.class);
    }

    private static SharedPreferencesStorage getStorage(Context context) {
        if (storage == null) {
            storage = new SharedPreferencesStorage(context);
        }
        return storage;
    }

    public static SharedPreferences getPreferences(Context context) {
        SharedPreferencesStorage storage = getStorage(context);
        return storage.getSharedPreferences(context);
    }
}
