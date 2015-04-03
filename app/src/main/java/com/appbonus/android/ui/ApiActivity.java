package com.appbonus.android.ui;

import android.os.Bundle;

import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.api.model.LoginRequest;
import com.appbonus.android.api.model.RegisterRequest;
import com.appbonus.android.api.model.ResetPasswordRequest;
import com.appbonus.android.api.model.VkLoginRequest;
import com.appbonus.android.model.api.LoginWrapper;
import com.appbonus.android.model.api.QuestionsWrapper;
import com.appbonus.android.model.api.SimpleResult;
import com.dolphin.utils.KeyboardUtils;

public abstract class ApiActivity extends VkMusicActivity {
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new ApiImpl(this);
        KeyboardUtils.setupTouchEvents(this, getWindow().getDecorView());
    }

    protected LoginWrapper login(LoginRequest request) throws Throwable {
        return api.login(request);
    }

    protected LoginWrapper vkLogin(VkLoginRequest request) throws Throwable {
        return api.vkLogin(request);
    }

    protected LoginWrapper vkRegistration(VkLoginRequest request) throws Throwable {
        return api.vkRegister(request);
    }

    protected LoginWrapper registration(RegisterRequest request) throws Throwable {
        return api.registration(request);
    }

    protected SimpleResult resetPassword(ResetPasswordRequest request) throws Throwable{
        return api.resetPassword(request);
    }

    protected QuestionsWrapper getFaq() throws Throwable {
        return api.getFaq();
    }
}
