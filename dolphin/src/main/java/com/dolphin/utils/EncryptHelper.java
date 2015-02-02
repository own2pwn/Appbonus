package com.dolphin.utils;

import java.security.MessageDigest;

public class EncryptHelper {
    public static String encryptMD5(String password) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(password.getBytes("utf8"));
            byte[] bytes = instance.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
