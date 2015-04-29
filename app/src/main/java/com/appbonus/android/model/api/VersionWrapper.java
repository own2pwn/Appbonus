package com.appbonus.android.model.api;

import java.io.Serializable;

public class VersionWrapper implements Serializable {
    protected String fileUrl;
    protected int version;

    public String getUrl() {
        return fileUrl;
    }

    public int getVersion() {
        return version;
    }
}
