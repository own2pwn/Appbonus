package com.appbonus.android.api.model;

public class VkLoginRequest {
    protected User user;

    public VkLoginRequest() {
        user = new User();
    }

    public VkLoginRequest(String vkToken, String email, String phone, String promo) {
        this(vkToken);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPromoCode(promo);
    }

    public VkLoginRequest(String vkToken) {
        this();
        user.setVkToken(vkToken);
    }

    static class User {
        protected String vkToken;
        protected String email;
        protected String phone;
        protected String promoCode;

        public void setVkToken(String vkToken) {
            this.vkToken = vkToken;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setPromoCode(String promoCode) {
            this.promoCode = promoCode;
        }
    }
}
