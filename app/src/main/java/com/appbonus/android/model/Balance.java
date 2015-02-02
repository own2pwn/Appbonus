package com.appbonus.android.model;

import java.io.Serializable;

public class Balance implements Serializable {
    protected Double activeBalance;
    protected Double heldBalance;
    protected Double pendingWithdrawal;
    protected Double referralsProfit;
    protected Double tasksProfit;
    protected Double withdrawalSum;

    public Double getActiveBalance() {
        return activeBalance;
    }

    public void setActiveBalance(Double activeBalance) {
        this.activeBalance = activeBalance;
    }

    public Double getHeldBalance() {
        return heldBalance;
    }

    public void setHeldBalance(Double heldBalance) {
        this.heldBalance = heldBalance;
    }

    public Double getPendingWithdrawal() {
        return pendingWithdrawal;
    }

    public void setPendingWithdrawal(Double pendingWithdrawal) {
        this.pendingWithdrawal = pendingWithdrawal;
    }

    public Double getReferralsProfit() {
        return referralsProfit;
    }

    public void setReferralsProfit(Double referralsProfit) {
        this.referralsProfit = referralsProfit;
    }

    public Double getTasksProfit() {
        return tasksProfit;
    }

    public void setTasksProfit(Double tasksProfit) {
        this.tasksProfit = tasksProfit;
    }

    public Double getWithdrawalSum() {
        return withdrawalSum;
    }

    public void setWithdrawalSum(Double withdrawalSum) {
        this.withdrawalSum = withdrawalSum;
    }
}
