package com.appbonus.android.loaders;

import android.content.Context;

import com.appbonus.android.api.Api;
import com.appbonus.android.api.model.SimpleRequest;
import com.appbonus.android.model.api.BalanceWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.dolphin.loader.AbstractLoader;

public class BalanceLoader extends AbstractLoader<BalanceWrapper> {
    protected Api api;

    public BalanceLoader(Context context, Api api) {
        super(context);
        this.api = api;
    }

    @Override
    protected BalanceWrapper backgroundLoading() throws Throwable {
        return api.readBalance(new SimpleRequest(SharedPreferencesStorage.getToken(getContext())));
    }
}
