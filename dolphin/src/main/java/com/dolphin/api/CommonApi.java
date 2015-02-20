package com.dolphin.api;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import com.dolphin.json.JsonHandler;
import com.dolphin.net.methods.HttpMethod;
import com.dolphin.net.methods.MethodDelete;
import com.dolphin.net.methods.MethodGet;
import com.dolphin.net.methods.MethodPost;
import com.dolphin.net.methods.MethodPostFile;
import com.dolphin.net.methods.MultipartUploader;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class CommonApi {
    private static final int NANO_PER_MILLS = 1000 * 1000;
    private static final String MILLS = "ms.";
    private static final String LOG_TAG = "Log path - %s; Date - %s";

    protected Context context;

    protected CommonApi(Context context) {
        this.context = context;
    }

    class ApiLogger {
        private long start;
        private long end;

        public void start() {
            start = System.nanoTime();
        }

        public void end() {
            end = System.nanoTime();
        }

        public void end(String tag) {
            end();
            logTime(tag);
        }

        public void logTime(String tag) {
            long time = end - start;
            tag = String.format(LOG_TAG, tag, DateFormat.format("dd/MM/yyyy HH:mm:ss", new Date()));
            String duration = String.valueOf(time / (NANO_PER_MILLS)) + MILLS;
            Log.i(tag, duration);
            report(tag, duration);
        }
    }

    protected <T, K> T doPost(K request, Class<K> requestType, Class<T> responseType, String... path) throws Throwable {
        String[] array = collectParameters(path);
        HttpMethod method = new MethodPost(host(), toJson(request, requestType), array);
        preparation(method);
        ApiLogger logger = new ApiLogger();
        logger.start();
        String answer = method.perform(context);
        logger.end(Arrays.toString(path));
        return toObject(answer, responseType);
    }

    protected <T, K> T doGet(K request, Class<K> requestType, Class<T> responseType, String... path) throws Throwable {
        String[] array = collectParameters(path);
        HttpMethod method = new MethodGet(host(), toMap(request, requestType), array);
        preparation(method);
        ApiLogger logger = new ApiLogger();
        logger.start();
        String answer = method.perform(context);
        logger.end(Arrays.toString(path));
        return toObject(answer, responseType);
    }

    protected <T, K> T doDelete(K request, Class<K> requestType, Class<T> responseType, String... path) throws Throwable {
        String[] array = collectParameters(path);
        HttpMethod method = new MethodDelete(host(), toMap(request, requestType), array);
        preparation(method);
        ApiLogger logger = new ApiLogger();
        logger.start();
        String answer = method.perform(context);
        logger.end(Arrays.toString(path));
        return toObject(answer, responseType);
    }

    protected <T, K> T doUpload(K request, Class<K> requestType, Class<T> responseType, String... path) throws Throwable {
        String[] array = collectParameters(path);
        HttpMethod method = new MultipartUploader(host(), toUploadMap(request, requestType), array);
        preparation(method);
        ApiLogger logger = new ApiLogger();
        logger.start();
        String answer = method.perform(context);
        logger.end(Arrays.toString(path));
        return toObject(answer, responseType);
    }

    protected <T, K> T doPostUpload(K request, Class<K> requestType, Class<T> responseType, String... path) throws Throwable {
        String[] array = collectParameters(path);
        HttpMethod method = new MethodPostFile(host(), toUploadMap(request, requestType), array);
        preparation(method);
        ApiLogger logger = new ApiLogger();
        logger.start();
        String answer = method.perform(context);
        logger.end(Arrays.toString(path));
        return toObject(answer, responseType);
    }

    private <K> Map<String, File> toUploadMap(K obj, Class<K> tClass) throws Throwable{
        if (obj == null) return new HashMap<>();
        Field[] fields = tClass.getFields();
        Map<String, File> map = new HashMap<>();
        if (fields != null) {
            for (Field field : fields) {
                if (File.class.equals(field.getType())) {
                    File file = (File) field.get(obj);
                    map.put(field.getName(), file);
                }
            }
        }
        return map;
    }

    private <K> Map<String, String> toMap(K obj, Class<K> tClass) {
        if (obj == null) return new HashMap<>();
        JsonHandler<K> jsonHandler = createJsonHandler(tClass);
        return jsonHandler.toMap(obj);
    }

    protected <T> JSONObject toJson(T obj, Class<T> tClass) throws Throwable {
        JsonHandler<T> jsonHandler = createJsonHandler(tClass);
        return jsonHandler.toJsonObject(obj);
    }

    protected  <T> T toObject(String string, Class<T> tClass) {
        JsonHandler<T> jsonHandler = createJsonHandler(tClass);
        return jsonHandler.fromJsonString(string);
    }

    private String[] collectParameters(String[] path) {
        String[] array = null;
        if (path != null) {
            String[] parameters = apiParameters();
            array = new String[path.length + parameters.length];
            System.arraycopy(parameters, 0, array, 0, parameters.length);
            System.arraycopy(path, 0, array, parameters.length, path.length);
        }
        return array;
    }

    public String getString(int resourceId) {
        return context.getString(resourceId);
    }

    protected <T> JsonHandler<T> createJsonHandler(Class<T> tClass) {
        return new JsonHandler<>(tClass);
    }

    protected abstract String host();

    protected abstract String[] apiParameters();

    protected abstract void preparation(HttpMethod method);

    protected abstract void report(String tag, String message);
}
