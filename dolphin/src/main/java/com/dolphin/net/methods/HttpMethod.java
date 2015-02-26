package com.dolphin.net.methods;


import android.content.Context;

import com.dolphin.net.exception.FormException;


public interface HttpMethod {
    String NET_PROBLEM_ERROR_CODE = "NET_PROBLEM_ERROR_CODE";

    String perform(Context context) throws Throwable;

    void addHeader(String key, String value);

    static interface ErrorHandler {
        FormException handle(String error);
    }

    public void setErrorHandler(ErrorHandler errorHandler);
}
