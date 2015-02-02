package com.dolphin.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {
    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void hideFragmentKeyboard(Fragment fragment) {
        if (fragment != null) {
            final InputMethodManager imm = (InputMethodManager) fragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = fragment.getView();
            if (view != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
