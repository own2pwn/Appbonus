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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isHeld() {
        return held;
    }

    public void setHeld(boolean held) {
        this.held = held;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public String getOfferIcon() {
        return offerIcon;
    }

    public void setOfferIcon(String offerIcon) {
        this.offerIcon = offerIcon;
    }
}
