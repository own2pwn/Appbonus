package com.appbonus.android.model.api;

import com.appbonus.android.model.Meta;
import com.appbonus.android.model.ReferralsHistory;

import java.io.Serializable;
import java.util.List;

public class ReferralsHistoryWrapper implements Serializable {
    protected Meta meta;
    protected List<ReferralsHistory> referralsHistory;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<ReferralsHistory> getReferralsHistory() {
        return referralsHistory;
    }

    public void setReferralsHistory(List<ReferralsHistory> referralsHistory) {
        this.referralsHistory = referralsHistory;
    }
}
