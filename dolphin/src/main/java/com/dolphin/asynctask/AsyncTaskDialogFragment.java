package com.dolphin.asynctask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class AsyncTaskDialogFragment extends DialogFragment {
    public static final String MESSAGE = "tag";

    public static AsyncTaskDialogFragment newInstance() {
        return new AsyncTaskDialogFragment();
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
