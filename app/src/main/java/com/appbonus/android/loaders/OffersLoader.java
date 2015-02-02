package com.appbonus.android.loaders;

import android.content.Context;

import com.appbonus.android.api.Api;
import com.appbonus.android.model.api.OffersWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.dolphin.loader.AbstractLoader;

public class OffersLoader extends AbstractLoader<OffersWrapper> {
    protected Api api;
    protected long page;

    public OffersLoader(Context context, Api api, long page) {
        super(context);
        this.api = api;
        this.page = page;
    }

    @Override
    protected OffersWrapper backgroundLoading() throws Throwable {
        return api.getOffers(getContext(), SharedPreferencesStorage.getToken(getContext()), page);
    }
}
