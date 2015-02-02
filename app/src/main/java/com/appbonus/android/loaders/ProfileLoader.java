package com.appbonus.android.loaders;

import android.content.Context;

import com.appbonus.android.api.Api;
import com.appbonus.android.model.api.UserWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.dolphin.loader.AbstractLoader;

public class ProfileLoader extends AbstractLoader<UserWrapper> {
    protected Api api;

    public ProfileLoader(Context context, Api api) {
        super(context);
        this.api = api;
    }

    @Override
    protected UserWrapper backgroundLoading() throws Throwable {
        return api.readProfile(getContext(), SharedPreferencesStorage.getToken(getContext()));
    }
}
