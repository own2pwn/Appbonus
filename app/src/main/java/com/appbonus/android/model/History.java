package com.appbonus.android.model;

import java.io.Serializable;
import java.util.Date;

public class History implements Serializable {
    public static final String OPERATION_TYPE_WITHDRAWAL = "withdrawal";
    public static final String OPERATION_TYPE_PROFIT = "installation";
    public static final String OPERATION_TYPE_IN_PROGRESS = "in_progress";
    public static final String OPERATION_TYPE_SIGN_UP = "partner_sign_up";
    public static final String OPERATION_TYPE_REFERRAL_INSTALLATION = "referral_installation";

    protected Double amount;
    protected Date createdAt;
    protected boolean held;
    protected Long id;
    protected String operationType;
    protected Long userId;
    protected String offerTitle;
    protected String offerIcon;
    protected String reference;

    public Double getAmount() {
        return amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean isHeld() {
        return held;
    }

    public Long getId() {
        return id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Long getUserId() {
        return userId;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public String getOfferIcon() {
        return offerIcon;
    }

    public String getReference() {
        return reference;
    }
}
