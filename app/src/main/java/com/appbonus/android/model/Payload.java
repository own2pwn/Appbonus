package com.appbonus.android.model;

import java.util.Date;

public class Payload {
    protected Integer referralLevel;
    protected Date createdDate;
    protected String operationType;

    public Integer getReferralLevel() {
        return referralLevel;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getOperationType() {
        return operationType;
    }
}
