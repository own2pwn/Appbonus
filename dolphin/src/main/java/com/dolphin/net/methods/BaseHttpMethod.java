package com.dolphin.net.methods;


import android.content.Context;
import android.net.Uri;

import com.dolphin.utils.ConnectionUtilsInsecure;
import com.dolphin.utils.Log;
import com.dolphin.utils.NetUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created at 04.12.13 19:43
 *
 * @author Altero
 */
public abstract class BaseHttpMethod implements HttpMethod {

    @SuppressWarnings("unused")
    private static final String TAG = Log.getNormalizedTag(BaseHttpMethod.class);

    private static final int TIMEOUT_MILLIS = 20000;

    private String hostUri;

    protected final String entity;
    protected final String[] apiPaths;
    protected final Uri.Builder uriBuilder;
    protected final Map<String, String> params;
    protected Map<String, String> headers;

    protected String requestMethod;
    protected HttpURLConnection connection;

    protected int responseCode;
    protected String responseEntity;
    protected String contentType = "application/json";
    protected ErrorHandler errorHandler;

    BaseHttpMethod(String hostUri, String json, String... apiPath) {

        this.hostUri = hostUri;
        this.entity = json;
        this.params = null;
        this.apiPaths = apiPath;
        this.uriBuilder = new Uri.Builder();
        this.requestMethod = "";
    }

    BaseHttpMethod(String hostUri, Map<String, String> params, String... apiPath) {

        this.hostUri = hostUri;
        this.params = params;
        this.entity = null;
        this.apiPaths = apiPath;
        this.uriBuilder = new Uri.Builder();
    }

    public String perform(Context context) throws Throwable {
        assert context != null;

        if (!NetUtils.isNetworkAvailable(context))
            throw new Throwable(NET_PROBLEM_ERROR_CODE);

        try {
            URL url = assembleUrl(this.apiPaths);
            configureConnection(url);

            this.connection.connect();
            writeToOpenedConnection(this.entity);

            this.responseEntity = getResponse();
            return this.responseEntity;
        } catch (IOException e) {
            throw new InvocationTargetException(e, e.getMessage());
        }
    }

    @Override
    public int getResponseCode() {

        return this.responseCode;
    }

    @Override
    public String getResponseEntity() {

        return this.responseEntity;
    }

    private URL assembleUrl(String... apiPath) throws Throwable {

        ConnectionUtilsInsecure.init(hostUri);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ConnectionUtilsInsecure.getHttpScheme());
        builder.encodedAuthority(ConnectionUtilsInsecure.getHostUrl());

        if (apiPath != null)
            for (String pathSegment : apiPath)
                builder.appendPath(pathSegment);

        if (params != null)
            for (Map.Entry<String, String> query : params.entrySet())
                builder.appendQueryParameter(query.getKey(), query.getValue());

        try {
            Uri uri = builder.build();
            return new URL(uri != null ? uri.toString() : "");
        } catch (MalformedURLException e) {
            throw new Throwable(WTF_ERROR_CODE, e);
        }

    }

    protected void configureConnection(URL postUrl) throws IOException {

        this.connection = (HttpURLConnection) postUrl.openConnection();
        this.connection.setConnectTimeout(TIMEOUT_MILLIS);
        this.connection.setRequestMethod(this.requestMethod);

        addHeaders(this.connection);
    }

    public void addHeaders(HttpURLConnection connection) {

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    private String readResponse(HttpURLConnection connection) throws IOException {

        return readFromStream(connection.getInputStream());
    }

    private String readError(HttpURLConnection connection) throws IOException {

        return readFromStream(connection.getErrorStream());
    }

    private String readFromStream(InputStream inputStream) throws IOException {

        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        StringBuilder builder = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }

        bufferedReader.close();
        return builder.toString();
    }

    protected void writeToOpenedConnection(String json) throws IOException {

        if (json == null)
            return;

        BufferedWriter outputStream = new BufferedWriter(
                new OutputStreamWriter(this.connection.getOutputStream())
        );
        outputStream.write(json);
        outputStream.flush();
        outputStream.close();
    }

    private String getResponse() throws Throwable {
        try {
            this.responseCode = this.connection.getResponseCode();
            Log.d(TAG, "Status code: " + responseCode);

            if (responseCode == SUCCESS_CODE)
                return readResponse(connection);
            else throw new Throwable();
        } catch (Throwable e) {
            return throwError();
        } finally {
            if (this.connection != null) {
                this.connection.disconnect();
            }
        }
    }

    private String throwError() throws Throwable {
        String detailMessage = readError(this.connection);
        if (errorHandler != null) {
            throw new Throwable(errorHandler.handle(detailMessage));
        }
        throw new Throwable(detailMessage);
    }

    @Override
    public List<String> getAuthErrors() {
        return new ArrayList<String>();
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put(key, value);
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
