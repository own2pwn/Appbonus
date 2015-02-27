package com.appbonus.android.model.api;

import com.appbonus.android.model.History;
import com.appbonus.android.model.Meta;

import java.io.Serializable;
import java.util.List;

public class HistoryWrapper implements Serializable {
    protected Meta meta;
    protected List<History> history;

    public Meta getMeta() {
        return meta;
    }

    public List<History> getHistory() {
        return history;
    }
}
