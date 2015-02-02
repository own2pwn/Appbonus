package com.dolphin.net.methods;


import java.util.Map;


/**
 * Created at 04.12.13 19:45
 *
 * @author Altero
 */
public class MethodDelete extends BaseHttpMethod {

    private static final String REQUEST_METHOD = "DELETE";

    public MethodDelete(String hostUri, Map<String, String> params, String... apiPath) {

        super(hostUri, params, apiPath);
        requestMethod = REQUEST_METHOD;
    }
}
