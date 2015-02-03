package com.appbonus.android.api.model;

public class RegisterRequest {
    protected User user;

    public RegisterRequest(String email, String password, String country, String phone, String deviceId) {
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setCountry(country);
        user.setPhone(phone);
        user.setDeviceId(deviceId);
    }

    static class User {
        protected String email;
        protected String password;
        protected String country;
        protected String phone;
        protected String deviceId;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
    }
}
