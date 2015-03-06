package com.appbonus.android.model.api;

import android.text.TextUtils;

import org.apache.commons.lang3.StringUtils;

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
        return StringUtils.defaultString(data);
    }

    public boolean isSuccess() {
        return !TextUtils.isEmpty(data);
    }
}
