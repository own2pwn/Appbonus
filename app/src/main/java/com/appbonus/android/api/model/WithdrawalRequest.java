package com.appbonus.android.api.model;

public class WithdrawalRequest extends SimpleRequest {
    protected com.appbonus.android.model.WithdrawalRequest withdrawalRequest;

    public com.appbonus.android.model.WithdrawalRequest getWithdrawalRequest() {
        return withdrawalRequest;
    }

    public void setWithdrawalRequest(com.appbonus.android.model.WithdrawalRequest withdrawalRequest) {
        this.withdrawalRequest = withdrawalRequest;
    }
}
