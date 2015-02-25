package com.appbonus.android.api.model;

public class RegisterRequest {
    protected User user;

    public RegisterRequest(String email, String password, String country, String phone, String deviceId, String promoCode) {
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setCountry(country);
        user.setPhone(phone);
        user.setDeviceId(deviceId);
        user.setPromoCode(promoCode);
    }

    static class User {
        protected String email;
        protected String password;
        protected String country;
        protected String phone;
        protected String deviceId;
        protected String promoCode;

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public void setPromoCode(String promoCode) {
            this.promoCode = promoCode;
        }
    }
}
