package com.appbonus.android.model.api;

import java.io.Serializable;

public class VersionWrapper implements Serializable {
    protected String url;
    protected int version;

    public String getUrl() {
        return url;
    }

    public int getVersion() {
        return version;
    }
}
