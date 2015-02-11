package com.dolphin.asynctask;

import android.content.Context;
import android.os.AsyncTask;

public abstract class ExceptionAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    protected Context context;
    protected Throwable throwable;

    protected ExceptionAsyncTask(Context context) {
        this.context = context;
    }

    public boolean isSuccess() {
        return throwable == null;
    }

    @Override
    protected Result doInBackground(Params... params) {
        try {
            return background(params);
        } catch (Throwable throwable) {
            this.throwable = throwable;
            return null;
        }
    }

    protected abstract Result background(Params... params) throws Throwable;
}
