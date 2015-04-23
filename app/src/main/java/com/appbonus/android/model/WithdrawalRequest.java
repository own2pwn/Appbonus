package com.appbonus.android.model;

import java.io.Serializable;

public class WithdrawalRequest implements Serializable {
    public static final String PHONE_REQUEST_TYPE = "phone";
    public static final String QIWI_REQUEST_TYPE = "qiwi";

    protected String requestType;
    protected Double amount;
    protected String phone;

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
