package com.appbonus.android.ui;

import android.app.AlertDialog;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;

public abstract class BonusVkSdkListener extends VKSdkListener {

    @Override
    public void onCaptchaError(VKError captchaError) {
        new VKCaptchaDialog(captchaError).show();
    }

    @Override
    public void onTokenExpired(VKAccessToken vkAccessToken) {
        VKSdk.authorize(VkActivity.scope);
    }

    @Override
    public void onAccessDenied(VKError authorizationError) {
        if (authorizationError.errorCode != VKError.VK_CANCELED) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
        }
    }

    @Override
    public void onReceiveNewToken(VKAccessToken newToken) {
        onToken(newToken.accessToken);
    }

    @Override
    public void onAcceptUserToken(VKAccessToken token) {
        onToken(token.accessToken);
    }

    protected abstract void onToken(String token);
}
