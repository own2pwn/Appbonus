package com.dolphin.json;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonHandler<T> {

    private static Gson gsonParser = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .registerTypeAdapter(Date.class, new DateSerializer())
            .create();

    private Class<T> mClass;

    public JsonHandler(Class<T> aClass) {
        mClass = aClass;
    }

    public JSONObject toJsonObject(T object) throws Throwable {

        try {
            return new JSONObject(toJsonString(object));
        }
        catch (JSONException e) {
            throw new Throwable();
        }
    }

    public String toJsonString(T object) {

        return gsonParser.toJson(object, mClass);
    }

    public T fromJsonObject(JSONObject object) {

        return gsonParser.fromJson(object.toString(), mClass);
    }

    public T fromJsonString(String jsonString) {

        return gsonParser.fromJson(jsonString, mClass);
    }

    public List<T> listFromJsonString(String jsonString) {

        Type listOfTestObject = new TypeToken<List<T>>(){}.getType();
        List<Map> maps = gsonParser.fromJson(jsonString, listOfTestObject);
        List<T> result = new ArrayList<T>(maps.size());
        for (Map map : maps) {
            String tmpJson = gsonParser.toJson(map);
            result.add(gsonParser.fromJson(tmpJson, mClass));
        }
        return result;
    }

    public String listToJsonString(List<T> list) {
        JsonElement element = gsonParser.toJsonTree(list, new TypeToken<List<T>>() {}.getType());

        return element.getAsJsonArray().toString();
    }

    public Map<String, String> toMap(T object) {
        String s = toJsonString(object);
        return gsonParser.fromJson(s, new TypeToken<HashMap<String, String>>() {}.getType());
    }

    private static class DateSerializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String date = json.getAsString();
            if (StringUtils.isNoneBlank(date)) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(date);
                } catch (ParseException e) {
                    return null;
                }
            }
            return null;
        }
    }
}
