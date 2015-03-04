package com.appbonus.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.appbonus.android.model.enums.Sex;
import com.appbonus.android.model.enums.UserStatus;

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
    protected UserStatus status;
    //device_ids
    //device_tokens
    protected String postbackLink;
    protected boolean notifySound;
    protected boolean phoneConfirmed;
    protected Date phoneConfirmedAt;
    protected List<AuthService> authServices;
    protected String inviteCode;
    protected Date birthDate;
    protected Sex gender;
    protected String name;

    public long getId() {
        return id;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getBalance() {
        return balance;
    }

    public boolean isIphone() {
        return iphone;
    }

    public boolean isIpad() {
        return ipad;
    }

    public boolean isAndroid() {
        return android;
    }

    public Long getReferrerId() {
        return referrerId;
    }

    public String getYmToken() {
        return ymToken;
    }

    public String getTester() {
        return tester;
    }

    public String getFtxSearch() {
        return ftxSearch;
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

    public UserStatus getStatus() {
        return status;
    }

    public String getPostbackLink() {
        return postbackLink;
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

    public Date getPhoneConfirmedAt() {
        return phoneConfirmedAt;
    }

    public List<AuthService> getAuthServices() {
        return authServices;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Sex getGender() {
        return gender;
    }

    public void setGender(Sex gender) {
        this.gender = gender;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public User() {
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
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeString(this.postbackLink);
        dest.writeByte(notifySound ? (byte) 1 : (byte) 0);
        dest.writeByte(phoneConfirmed ? (byte) 1 : (byte) 0);
        dest.writeLong(phoneConfirmedAt != null ? phoneConfirmedAt.getTime() : -1);
        dest.writeTypedList(authServices);
        dest.writeString(this.inviteCode);
        dest.writeLong(birthDate != null ? birthDate.getTime() : -1);
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        dest.writeString(this.name);
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
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : UserStatus.values()[tmpStatus];
        this.postbackLink = in.readString();
        this.notifySound = in.readByte() != 0;
        this.phoneConfirmed = in.readByte() != 0;
        long tmpPhoneConfirmedAt = in.readLong();
        this.phoneConfirmedAt = tmpPhoneConfirmedAt == -1 ? null : new Date(tmpPhoneConfirmedAt);
        authServices = new ArrayList<>();
        in.readTypedList(authServices, AuthService.CREATOR);
        this.inviteCode = in.readString();
        long tmpBirthDate = in.readLong();
        this.birthDate = tmpBirthDate == -1 ? null : new Date(tmpBirthDate);
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Sex.values()[tmpGender];
        this.name = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
