package com.appbonus.android.model.api;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class DataWrapper implements Serializable {
    protected String data;
    protected List<String> errors;

    public String getData() {
        return data;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        if (!TextUtils.isEmpty(data)) {
            return data;
        }
        return "";
    }

    public boolean isSuccess() {
        return !TextUtils.isEmpty(data);
    }
}
