package com.appbonus.android.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class User implements Serializable {
    public static final String COUNTRY_RUSSIA = "russia";
    public static final String COUNTRY_BELARUS = "belarus";
    public static final String COUNTRY_UKRAINE = "ukraine";

    protected long id;
    protected String email;
    protected Date createAt;
    protected Date updatedAt;
    protected String phone;
    protected String country;
    protected String role;
    protected Double balance;
    protected boolean iphone;
    protected boolean ipad;
    protected boolean android;
    protected Long referrerId;
    protected String ymToken;
    protected String tester;
    protected String ftxSearch;
    protected boolean notify;
    protected String refRate;
    protected String status;
    //device_ids
    //device_tokens
    protected String postbackLink;
    protected boolean notifySound;
    protected boolean phoneConfirmed;
    protected Date phoneConfirmedAt;
    protected List<AuthService> authServices;
    protected String promoCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public boolean isIphone() {
        return iphone;
    }

    public void setIphone(boolean iphone) {
        this.iphone = iphone;
    }

    public boolean isIpad() {
        return ipad;
    }

    public void setIpad(boolean ipad) {
        this.ipad = ipad;
    }

    public boolean isAndroid() {
        return android;
    }

    public void setAndroid(boolean android) {
        this.android = android;
    }

    public Long getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(Long referrerId) {
        this.referrerId = referrerId;
    }

    public String getYmToken() {
        return ymToken;
    }

    public void setYmToken(String ymToken) {
        this.ymToken = ymToken;
    }

    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }

    public String getFtxSearch() {
        return ftxSearch;
    }

    public void setFtxSearch(String ftxSearch) {
        this.ftxSearch = ftxSearch;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public String getRefRate() {
        return refRate;
    }

    public void setRefRate(String refRate) {
        this.refRate = refRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPostbackLink() {
        return postbackLink;
    }

    public void setPostbackLink(String postbackLink) {
        this.postbackLink = postbackLink;
    }

    public boolean isNotifySound() {
        return notifySound;
    }

    public void setNotifySound(boolean notifySound) {
        this.notifySound = notifySound;
    }

    public boolean isPhoneConfirmed() {
        return phoneConfirmed;
    }

    public void setPhoneConfirmed(boolean phoneConfirmed) {
        this.phoneConfirmed = phoneConfirmed;
    }

    public Date getPhoneConfirmedAt() {
        return phoneConfirmedAt;
    }

    public void setPhoneConfirmedAt(Date phoneConfirmedAt) {
        this.phoneConfirmedAt = phoneConfirmedAt;
    }

    public List<AuthService> getAuthServices() {
        return authServices;
    }

    public void setAuthServices(List<AuthService> authServices) {
        this.authServices = authServices;
    }

    public String getPromoCode() {
        return promoCode;
    }
}
