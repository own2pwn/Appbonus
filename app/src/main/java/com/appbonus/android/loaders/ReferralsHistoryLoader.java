package com.appbonus.android.loaders;

import android.content.Context;

import com.appbonus.android.api.Api;
import com.appbonus.android.api.model.PagingRequest;
import com.appbonus.android.model.api.ReferralsHistoryWrapper;
import com.dolphin.loader.AbstractLoader;

public class ReferralsHistoryLoader extends AbstractLoader<ReferralsHistoryWrapper> {
    protected Api api;
    protected Long page;

    public ReferralsHistoryLoader(Context context, Api api, Long page) {
        super(context);
        this.page = page;
        this.api = api;
    }

    @Override
    protected ReferralsHistoryWrapper backgroundLoading() throws Throwable {
        return api.readReferralsHistory(new PagingRequest(page));
    }
}
