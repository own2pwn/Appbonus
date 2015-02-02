package com.dolphin.net.methods;


import java.io.IOException;
import java.net.URL;
import java.util.Map;


/**
 * Created at 04.12.13 19:45
 *
 * @author Altero
 */
public class MethodGet extends BaseHttpMethod {

    private static final String REQUEST_METHOD = "GET";

    public MethodGet(String hostUri, Map<String, String> params, String... apiPath) {

        super(hostUri, params, apiPath);
        requestMethod = REQUEST_METHOD;
    }

    @Override
    protected void configureConnection(URL postUrl) throws IOException {

        super.configureConnection(postUrl);
        this.connection.setDoInput(true);
    }
}
