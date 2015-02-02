package com.dolphin.net.methods;


import android.content.Context;

import java.util.List;
import java.util.Map;


public interface HttpMethod {

    String NET_PROBLEM_ERROR_CODE = "NET_PROBLEM_ERROR_CODE";
    String WTF_ERROR_CODE = "WTF_ERROR_CODE";

    int SUCCESS_CODE = 200;
    /**
     * Start, connect and fetch data from predefined request
     *
     * @param context
     *         Context to check NetworkState
     *
     * @return JSON-formatted response entity
     */
    String perform(Context context) throws Throwable;


    int getResponseCode();

    /**
     * Get already fetched data
     *
     * @return JSON-formatted response entity
     */
    String getResponseEntity();

    List<String> getAuthErrors();

    void setHeaders(Map<String, String> headers);

    void addHeader(String key, String value);
}
