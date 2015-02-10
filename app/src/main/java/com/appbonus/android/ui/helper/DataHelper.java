package com.appbonus.android.ui.helper;

import android.content.Context;

import com.appbonus.android.model.User;
import com.appbonus.android.model.api.LoginWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;

public class DataHelper {
    public static void saveInfo(Context context, LoginWrapper loginWrapper) {
        SharedPreferencesStorage.saveToken(context, loginWrapper.getAuthToken());
        User user = loginWrapper.getUser();
        if (user != null) {
            SharedPreferencesStorage.saveUser(context, user);
            SharedPreferencesStorage.confirmPhone(context, user.isPhoneConfirmed());
            SharedPreferencesStorage.saveUserId(context, user.getId());
            if (user.getBalance() != null)
                SharedPreferencesStorage.saveBalance(context, user.getBalance());
        }
    }
}
