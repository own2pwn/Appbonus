package com.appbonus.android.component;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.appbonus.android.R;
import com.dolphin.asynctask.ExceptionAsyncTask;

public abstract class DialogExceptionalAsyncTask<Params, Progress, Result> extends ExceptionAsyncTask<Params, Progress, Result> {
    protected AsyncTaskDialogFragment dialogFragment;

    protected DialogExceptionalAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialogFragment = AsyncTaskDialogFragment.newInstance();
        dialogFragment.setArguments(createArguments());
        dialogFragment.show(getFragmentManager(), "dialogFragment");
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        closeQuietly(dialogFragment);
    }

    protected void closeQuietly(DialogFragment fragment) {
        if (fragment != null) {
            fragment.dismissAllowingStateLoss();
        }
    }

    protected abstract FragmentManager getFragmentManager();

    protected String message() {
        return context.getResources().getString(R.string.loading);
    }

    protected Bundle createArguments() {
        Bundle bundle = new Bundle();
        bundle.putString(AsyncTaskDialogFragment.MESSAGE, message());
        return bundle;
    }
}
