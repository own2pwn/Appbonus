package com.appbonus.android.ui.helper;

import android.content.Context;

import com.appbonus.android.model.User;
import com.appbonus.android.model.api.LoginWrapper;
import com.appbonus.android.storage.Config;
import com.appbonus.android.storage.Storage;

public class DataHelper {
    public static void saveInfo(Context context, LoginWrapper loginWrapper) {
        Storage.save(context, Config.TOKEN, loginWrapper.getAuthToken());
        User user = loginWrapper.getUser();
        if (user != null) {
            Storage.save(context, Config.USER, user);
            Storage.save(context, Config.PHONE_CONFIRMED, user.isPhoneConfirmed());
            Storage.save(context, Config.USER_ID, user.getId());
            if (user.getBalance() != null)
                Storage.save(context, Config.BALANCE, user.getBalance());
        }
    }
}
