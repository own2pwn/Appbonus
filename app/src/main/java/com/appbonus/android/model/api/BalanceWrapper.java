package com.appbonus.android.model.api;

import com.appbonus.android.model.Balance;

import java.io.Serializable;

public class BalanceWrapper implements Serializable {
    protected Balance balance;

    public Balance getBalance() {
        return balance;
    }
}
