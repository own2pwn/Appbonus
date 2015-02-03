package com.appbonus.android.api;

import android.content.Context;
import android.util.Log;

import com.appbonus.android.R;
import com.dolphin.json.JsonHandler;
import com.dolphin.net.methods.HttpMethod;
import com.dolphin.net.methods.MethodGet;
import com.dolphin.net.methods.MethodPost;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public abstract class CommonApi {
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
            Log.i(tag, String.valueOf(time / (1000 * 1000)) + "ms.");
        }
    }

    class ApiErrorHandler implements HttpMethod.ErrorHandler {
        private static final String ERROR_PARAMETER = "error";
        private static final String ERRORS_PARAMETER = "errors";
        private static final String SUCCESS_PARAMETER = "success";

        protected CommonApi api;

        public ApiErrorHandler(CommonApi api) {
            this.api = api;
        }

        @Override
        public String handle(String error) {
            try {
                JSONObject object = new JSONObject(error);
                if (object.has(ERROR_PARAMETER)) {
                    return object.getString(ERROR_PARAMETER);
                } else if (object.has(ERRORS_PARAMETER)) {
                    JSONObject errors = object.getJSONObject(ERRORS_PARAMETER);
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()) {
                        String next = keys.next();
                        Object o = errors.get(next);
                        if (o instanceof JSONArray) {
                            return WordUtils.capitalize(next) + " " + ((JSONArray) o).get(0);
                        } else return WordUtils.capitalize(errors.optString(next));
                    }
                } else if (object.has(SUCCESS_PARAMETER)) {
                    boolean aBoolean = object.getBoolean(SUCCESS_PARAMETER);
                    if (!aBoolean) {
                        return api.getString(R.string.failed);
                    } else return api.getString(R.string.success);
                }
            } catch (JSONException ignored) {
            }
            return error;
        }
    }

    protected <T, K> T doPost(K request, Class<K> requestType, Class<T> responseType, String... path) throws Throwable {
        String[] array = null;
        if (path != null) {
            String[] parameters = apiParameters();
            array = new String[path.length + parameters.length];
            System.arraycopy(parameters, 0, array, 0, parameters.length);
            System.arraycopy(path, 0, array, 2, path.length);
        }
        HttpMethod method = new MethodPost(host(), toJson(request, requestType), array);
        String answer = method.perform(context);
        return toObject(answer, responseType);
    }

    protected <T, K> T doGet(K request, Class<K> requestType, Class<T> responseType, String... path) throws Throwable {
        String[] array = null;
        if (path != null) {
            String[] parameters = apiParameters();
            array = new String[path.length + parameters.length];
            System.arraycopy(parameters, 0, array, 0, parameters.length);
            System.arraycopy(path, 0, array, 2, path.length);
        }
        HttpMethod method = new MethodGet(host(), toMap(request, requestType), array);
        String answer = method.perform(context);
        return toObject(answer, responseType);
    }

    private <K> Map<String, String> toMap(K request, Class<K> tClass) throws IllegalAccessException {
        JsonHandler<K> jsonHandler = new JsonHandler<>(tClass);
        return jsonHandler.toMap(request);
    }

    protected <T> JSONObject toJson(T obj, Class<T> tClass) throws Throwable {
        JsonHandler<T> jsonHandler = new JsonHandler<>(tClass);
        return jsonHandler.toJsonObject(obj);
    }

    protected  <T> T toObject(String string, Class<T> tClass) {
        JsonHandler<T> jsonHandler = new JsonHandler<>(tClass);
        return jsonHandler.fromJsonString(string);
    }

    public abstract String host();

    public abstract String[] apiParameters();

    public String getString(int resourceId) {
        return context.getString(resourceId);
    }
}
