package com.appbonus.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User implements Serializable, Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.email);
        dest.writeLong(createAt != null ? createAt.getTime() : -1);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
        dest.writeString(this.phone);
        dest.writeString(this.country);
        dest.writeString(this.role);
        dest.writeValue(this.balance);
        dest.writeByte(iphone ? (byte) 1 : (byte) 0);
        dest.writeByte(ipad ? (byte) 1 : (byte) 0);
        dest.writeByte(android ? (byte) 1 : (byte) 0);
        dest.writeValue(this.referrerId);
        dest.writeString(this.ymToken);
        dest.writeString(this.tester);
        dest.writeString(this.ftxSearch);
        dest.writeByte(notify ? (byte) 1 : (byte) 0);
        dest.writeString(this.refRate);
        dest.writeString(this.status);
        dest.writeString(this.postbackLink);
        dest.writeByte(notifySound ? (byte) 1 : (byte) 0);
        dest.writeByte(phoneConfirmed ? (byte) 1 : (byte) 0);
        dest.writeLong(phoneConfirmedAt != null ? phoneConfirmedAt.getTime() : -1);
        dest.writeList(this.authServices);
        dest.writeString(this.promoCode);
    }

    public User() {
    }

    private User(Parcel in) {
        this.id = in.readLong();
        this.email = in.readString();
        long tmpCreateAt = in.readLong();
        this.createAt = tmpCreateAt == -1 ? null : new Date(tmpCreateAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        this.phone = in.readString();
        this.country = in.readString();
        this.role = in.readString();
        this.balance = (Double) in.readValue(Double.class.getClassLoader());
        this.iphone = in.readByte() != 0;
        this.ipad = in.readByte() != 0;
        this.android = in.readByte() != 0;
        this.referrerId = (Long) in.readValue(Long.class.getClassLoader());
        this.ymToken = in.readString();
        this.tester = in.readString();
        this.ftxSearch = in.readString();
        this.notify = in.readByte() != 0;
        this.refRate = in.readString();
        this.status = in.readString();
        this.postbackLink = in.readString();
        this.notifySound = in.readByte() != 0;
        this.phoneConfirmed = in.readByte() != 0;
        long tmpPhoneConfirmedAt = in.readLong();
        this.phoneConfirmedAt = tmpPhoneConfirmedAt == -1 ? null : new Date(tmpPhoneConfirmedAt);
        this.authServices = new ArrayList<>();
        in.readList(this.authServices, List.class.getClassLoader());
        this.promoCode = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
