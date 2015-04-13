package com.dolphin.net.methods;


import android.content.Context;
import android.net.Uri;
import android.util.LruCache;

import com.dolphin.net.ConnectionUtilsInsecure;
import com.dolphin.net.NetUtils;
import com.dolphin.net.exception.NetworkException;
import com.dolphin.net.exception.UnauthorizedException;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public abstract class BaseHttpMethod implements HttpMethod {
    private static final int TIMEOUT_MILLIS = 20000;
    private static final int REQUEST_CACHE_SIZE = 1 * 1024 * 1024;
    private static final int RESPONSE_CACHE_SIZE = 1 * 1024 * 1024;

    private String hostUri;

    protected final String entity;
    protected final String[] apiPaths;
    protected final Uri.Builder uriBuilder;
    protected final Map<String, String> params;
    protected Map<String, String> headers;

    protected String requestMethod;
    protected HttpURLConnection connection;

    protected ErrorHandler errorHandler;

    static final LruCache<Integer, String> requestCache = new LruCache<>(REQUEST_CACHE_SIZE);
    static final LruCache<String, String> responseCache = new LruCache<>(RESPONSE_CACHE_SIZE);

    protected BaseHttpMethod(String hostUri, String json, String... apiPath) {
        this(hostUri, json, null, apiPath);
    }

    protected BaseHttpMethod(String hostUri, Map<String, String> params, String... apiPath) {
        this(hostUri, null, params, apiPath);
    }

    private BaseHttpMethod(String hostUri, String json, Map<String, String> params, String... apiPath) {
        this.hostUri = hostUri;
        this.params = params;
        this.entity = json;
        this.apiPaths = apiPath;
        this.uriBuilder = new Uri.Builder();
        this.requestMethod = "";
    }

    public String perform(Context context) throws Throwable {
        if (!NetUtils.isNetworkAvailable(context))
            throw new NetworkException();

        configureConnection(assembleUrl(apiPaths));
        connect();
        writeToOpenedConnection(entity);
        return getResponse();
    }

    private void connect() throws IOException {
        connection.connect();
    }

    private URL assembleUrl(String... apiPath) throws Throwable {
        ConnectionUtilsInsecure.init(hostUri);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ConnectionUtilsInsecure.getHttpScheme());
        builder.encodedAuthority(ConnectionUtilsInsecure.getHostUrl());

        if (apiPath != null) {
            for (String pathSegment : apiPath) {
                builder.appendPath(pathSegment);
            }
        }

        if (params != null) {
            for (Map.Entry<String, String> query : params.entrySet()) {
                builder.appendQueryParameter(query.getKey(), query.getValue());
            }
        }

        Uri uri = builder.build();
        return new URL(uri != null ? Uri.decode(uri.toString()) : "");
    }

    protected void configureConnection(URL postUrl) throws IOException {
        connection = (HttpURLConnection) postUrl.openConnection();
        connection.setConnectTimeout(TIMEOUT_MILLIS);
        connection.setRequestMethod(requestMethod);

        addETagHeaderIfExists();
        addHeaders();
    }

    private void addETagHeaderIfExists() {
        String tag = getRequestTag(connectionHash());
        if (tag != null) {
            addHeader(HttpHeaders.IF_NONE_MATCH, tag);
        }
    }

    public void addHeaders() {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        return readStream(connection.getInputStream());
    }

    private String readError(HttpURLConnection connection) {
        try {
            return readStream(connection.getErrorStream());
        } catch (Exception e) {
            return "Unknown error";
        }
    }

    private String readStream(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    protected void writeToOpenedConnection(String json) throws IOException {
        if (json == null)
            return;

        BufferedWriter outputStream = new BufferedWriter(
                new OutputStreamWriter(connection.getOutputStream())
        );
        outputStream.write(json);
        outputStream.flush();
        outputStream.close();
    }

    private String getResponse() throws Throwable {
        int responseCode;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            //http://stackoverflow.com/questions/17121213/java-io-ioexception-no-authentication-challenges-found
            responseCode = HttpStatus.SC_UNAUTHORIZED;
        }
        try {

            String eTag = connection.getHeaderField(HttpHeaders.ETAG);
            if (responseCode >= HttpStatus.SC_OK && responseCode < HttpStatus.SC_MULTIPLE_CHOICES) {
                saveRequest(connectionHash(), eTag);
                String response = readResponse(connection);
                saveResponse(eTag, response);
                return response;
            } else if (responseCode == HttpStatus.SC_NOT_MODIFIED) {
                return getResponse(eTag);
            } else if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
                UnauthorizedException exception = new UnauthorizedException();
                handleError(exception.getMessage());
                throw exception;
            }
            else return throwError();
        } finally {
            disconnectQuietly();
        }
    }

    private int connectionHash() {
        String url = connection.getURL().toString();
        return url.hashCode() + (entity != null ? entity.hashCode() : 0);
    }

    private String throwError() throws Throwable {
        String detailMessage = readError(connection);
        handleError(detailMessage);
        throw new Throwable(detailMessage);
    }

    private void handleError(String detailMessage) {
        if (errorHandler != null) {
            throw errorHandler.handle(detailMessage);
        }
    }

    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    private String getResponse(String tag) {
        synchronized (responseCache) {
            return responseCache.get(tag);
        }
    }

    private void saveResponse(String tag, String response) {
        if (tag == null || response == null) return;
        synchronized (responseCache) {
            responseCache.put(tag, response);
        }
    }

    private String getRequestTag(int hash) {
        synchronized (requestCache) {
            return requestCache.get(hash);
        }
    }

    private void saveRequest(int hash, String tag) {
        if (tag == null) return;
        synchronized (requestCache) {
            requestCache.put(hash, tag);
        }
    }

    private void disconnectQuietly() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    public static void resetCaches() {
        requestCache.evictAll();
        responseCache.evictAll();
    }
}
