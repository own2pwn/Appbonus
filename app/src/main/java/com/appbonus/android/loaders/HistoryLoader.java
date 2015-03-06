package com.appbonus.android.loaders;

import android.content.Context;

import com.appbonus.android.api.Api;
import com.appbonus.android.api.model.PagingRequest;
import com.appbonus.android.model.api.HistoryWrapper;
import com.dolphin.loader.AbstractLoader;

public class HistoryLoader extends AbstractLoader<HistoryWrapper> {
    protected Api api;
    protected Long page;

    public HistoryLoader(Context context, Api api, Long page) {
        super(context);
        this.page = page;
        this.api = api;
    }

    @Override
    protected HistoryWrapper backgroundLoading() throws Throwable {
        return api.readHistory(new PagingRequest(page));
    }
}
