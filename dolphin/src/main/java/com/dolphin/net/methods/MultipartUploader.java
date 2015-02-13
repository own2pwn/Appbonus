package com.dolphin.net.methods;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;


public class MultipartUploader extends BaseHttpMethod {
    private static final String REQUEST_METHOD = "PUT";

    private Map<String, File> files;
    private HttpEntity mHttpEntity;

    public MultipartUploader(String hostUri, Map<String, File> files, String... apiPath) {
        super(hostUri, "", apiPath);
        this.requestMethod = REQUEST_METHOD;
        this.files = files;
    }

    @Override
    protected void configureConnection(URL postUrl) throws IOException {
        MultipartEntityBuilder builder = MultipartEntityBuilder
                .create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (Map.Entry<String, File> entry : files.entrySet()) {
            builder.addPart(entry.getKey(), new FileBody(
                    entry.getValue(),
                    ContentType.create(URLConnection.guessContentTypeFromName(entry.getValue().getName())),
                    entry.getValue().getName()
            ));
        }

        mHttpEntity = builder.build();

        super.configureConnection(postUrl);

        connection.addRequestProperty("Content-length", mHttpEntity.getContentLength() + "");
        connection.addRequestProperty(
                mHttpEntity.getContentType().getName(),
                mHttpEntity.getContentType().getValue()
        );

        this.connection.setDoInput(true);
        this.connection.setDoOutput(true);
        this.connection.setUseCaches(false);
    }

    @Override
    protected void writeToOpenedConnection(String avatarPath) throws IOException {
        mHttpEntity.writeTo(connection.getOutputStream());
    }
}
