package com.appbonus.android.loaders;

import android.content.Context;

import com.appbonus.android.api.Api;
import com.appbonus.android.model.api.ReferralsDetailsWrapper;
import com.dolphin.loader.AbstractLoader;

public class ReferralsDetailsLoader extends AbstractLoader<ReferralsDetailsWrapper> {
    protected Api api;

    public ReferralsDetailsLoader(Context context, Api api) {
        super(context);
        this.api = api;
    }

    @Override
    protected ReferralsDetailsWrapper backgroundLoading() throws Throwable {
        return api.readReferralsDetails();
    }
}
