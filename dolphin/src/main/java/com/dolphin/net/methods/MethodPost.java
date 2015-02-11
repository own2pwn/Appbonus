package com.dolphin.net.methods;


import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;


public class MethodPost extends BaseHttpMethod {

    private static final String REQUEST_METHOD = "POST";

    @Deprecated
    public MethodPost(String hostUri, String json, String... apiPath) {
        super(hostUri, json, apiPath);
        requestMethod = REQUEST_METHOD;
    }

    public MethodPost(String hostUri, JSONObject json, String... apiPath) {
        this(hostUri, json.toString(), apiPath);
    }

    @Override
    protected void configureConnection(URL postUrl) throws IOException {
        super.configureConnection(postUrl);
        super.connection.setRequestProperty("Content-Type", contentType);
        super.connection.setDoInput(true);
        super.connection.setDoOutput(true);
    }
}
