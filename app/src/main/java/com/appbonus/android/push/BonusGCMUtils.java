package com.appbonus.android.push;

import android.content.Context;
import android.content.SharedPreferences;

import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.api.model.DeviceRequest;
import com.appbonus.android.storage.Storage;
import com.dolphin.push.GoogleCloudMessagingUtils;

public class BonusGCMUtils extends GoogleCloudMessagingUtils {
    @Override
    protected SharedPreferences getGCMPreferences(Context context) {
        return Storage.getPreferences(context);
    }

    @Override
    protected void sendRegistrationIdToBackend(Context context) throws Throwable {
        Api api = new ApiImpl(context);
        api.registerDevice(new DeviceRequest(regId));
    }
}
