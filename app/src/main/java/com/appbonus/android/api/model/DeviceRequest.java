package com.appbonus.android.api.model;

public class DeviceRequest extends SimpleRequest {
    protected String deviceToken;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
