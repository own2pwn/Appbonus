package com.dolphin.asynctask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

public class AsyncTaskDialogFragment extends DialogFragment {
    public static final String MESSAGE = "tag";

    public static AsyncTaskDialogFragment newInstance(Context context) {
        return (AsyncTaskDialogFragment) Fragment.instantiate(context, AsyncTaskDialogFragment.class.getName());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        Bundle arguments = getArguments();
        String text = arguments.getString(MESSAGE);
        dialog.setMessage(text);
        return dialog;
    }
}
