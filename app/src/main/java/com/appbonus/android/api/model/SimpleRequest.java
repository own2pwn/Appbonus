package com.appbonus.android.api.model;

public class SimpleRequest {
    protected String authToken;

    public SimpleRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
