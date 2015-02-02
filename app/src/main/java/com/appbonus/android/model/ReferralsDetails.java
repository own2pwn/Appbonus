package com.appbonus.android.model;

import java.util.List;

public class ReferralsDetails {
    protected Long userId;
    protected Double referralsProfit;
    protected List<Level> levels;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getReferralsProfit() {
        return referralsProfit;
    }

    public void setReferralsProfit(Double referralsProfit) {
        this.referralsProfit = referralsProfit;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }
}
