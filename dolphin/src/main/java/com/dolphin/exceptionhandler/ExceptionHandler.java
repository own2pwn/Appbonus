package com.dolphin.exceptionhandler;

import android.content.Context;

public interface ExceptionHandler {
    void register(Context context);

    void register(Context context, long projectID);

    void error(Throwable throwable);

    void error(Throwable throwable, String message);
}
