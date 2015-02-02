package com.dolphin.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;


public abstract class AbstractLoader<T> extends AsyncTaskLoader<T> {
    protected Throwable throwable;

    public AbstractLoader(Context context) {
        super(context);
    }

    public boolean isSuccess() {
        return throwable == null;
    }

    @Override
    public T loadInBackground() {
        try {
            return backgroundLoading();
        } catch (Throwable throwable) {
            this.throwable = throwable;
            return null;
        }
    }

    protected abstract T backgroundLoading() throws Throwable;
}
