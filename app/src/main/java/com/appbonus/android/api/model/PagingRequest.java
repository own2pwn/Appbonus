package com.appbonus.android.api.model;

public class PagingRequest extends SimpleRequest {
    protected Long page;

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }
}
