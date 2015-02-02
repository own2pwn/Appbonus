package com.appbonus.android.loaders;

import android.content.Context;

import com.appbonus.android.api.Api;
import com.appbonus.android.model.Offer;
import com.appbonus.android.model.api.OfferWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.dolphin.loader.AbstractLoader;

public class OfferLoader extends AbstractLoader<OfferWrapper> {
    protected Api api;
    protected Offer offer;

    public OfferLoader(Context context, Api api, Offer offer) {
        super(context);
        this.api = api;
        this.offer = offer;
    }

    @Override
    protected OfferWrapper backgroundLoading() throws Throwable {
        return api.showOffer(getContext(), SharedPreferencesStorage.getToken(getContext()), offer);
    }
}
