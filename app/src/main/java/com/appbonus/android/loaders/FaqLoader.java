package com.appbonus.android.loaders;

import android.content.Context;

import com.appbonus.android.api.Api;
import com.appbonus.android.api.model.SimpleRequest;
import com.appbonus.android.model.api.QuestionsWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.dolphin.loader.AbstractLoader;

public class FaqLoader extends AbstractLoader<QuestionsWrapper> {
    protected Api api;

    public FaqLoader(Context context, Api api) {
        super(context);
        this.api = api;
    }

    @Override
    protected QuestionsWrapper backgroundLoading() throws Throwable {
        return api.getFaq(new SimpleRequest(SharedPreferencesStorage.getToken(getContext())));
    }
}
