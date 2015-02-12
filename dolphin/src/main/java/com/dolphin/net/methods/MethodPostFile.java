package com.dolphin.net.methods;


import java.io.File;
import java.util.Map;


public class MethodPostFile extends MultipartUploader {

    private static final String REQUEST_METHOD_POST = "POST";

    public MethodPostFile(String hostUri, Map<String, File> files, String... apiPath) {
        super(hostUri, files, apiPath);
        this.requestMethod = REQUEST_METHOD_POST;
    }
}
