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

    public void setData(String data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
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
