package com.appbonus.android.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.appbonus.android.R;
import com.appbonus.android.api.model.LoginRequest;
import com.appbonus.android.api.model.ResetPasswordRequest;
import com.appbonus.android.api.model.VkLoginRequest;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.model.api.LoginWrapper;
import com.appbonus.android.model.api.SimpleResult;
import com.appbonus.android.push.BonusGCMUtils;
import com.appbonus.android.storage.Config;
import com.appbonus.android.storage.Storage;
import com.appbonus.android.ui.ApiActivity;
import com.appbonus.android.ui.VkMusicActivity;
import com.appbonus.android.ui.helper.DataHelper;
import com.appbonus.android.ui.helper.IntentHelper;
import com.dolphin.asynctask.DialogExceptionalAsyncTask;
import com.dolphin.push.GoogleCloudMessagingUtils;
import com.dolphin.push.OnGooglePlayServicesUnavailableListener;
import com.dynamixsoftware.ErrorAgent;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;

import java.lang.reflect.InvocationTargetException;

public class LoginActivity extends ApiActivity implements OnGooglePlayServicesUnavailableListener {
    private static final int REGISTRATION_INTENT_CODE = 1;
    private static final int RESET_PASSWORD_INTENT_CODE = 2;
    private static final int LOGIN_VK_CODE = 3;

    protected Form form;
    protected Form mailForm;

    protected FloatLabel mail;
    protected FloatLabel password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        VKSdk.initialize(sdkListener, getString(R.string.vk_id));
        initUI();
        initValidators();
    }

    private VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(sMyScope);
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
            onTokenReceive(newToken.accessToken);
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            onTokenReceive(token.accessToken);
        }
    };

    public void initUI() {
        mail = (FloatLabel) findViewById(R.id.login);
        password = (FloatLabel) findViewById(R.id.password);
    }

    public void initValidators() {
        form = new Form();
        Validate mailValidate = new Validate(mail.getEditText());
        mailValidate.addValidator(new EmailValidator(this, R.string.wrong_mail));
        mailValidate.addValidator(new NotEmptyValidator(this, R.string.input_mail));

        Validate passwordValidate = new Validate(password.getEditText());
        passwordValidate.addValidator(new NotEmptyValidator(this, R.string.input_password));

        form.addValidates(mailValidate);
        form.addValidates(passwordValidate);

        mailForm = new Form(this);
        mailForm.setShowFailValidateToast(true);
        mailForm.addValidates(mailValidate);
    }

    public void enterHandler(View view) {
        if (form.validate()) {
            String mailStr = mail.getText();
            final String passwordStr = password.getText();
            final LoginRequest loginRequest = new LoginRequest(mailStr, passwordStr);

            new DialogExceptionalAsyncTask<Void, Void, LoginWrapper>(this) {

                @Override
                protected LoginWrapper background(Void... params) throws Throwable {
                    return login(loginRequest);
                }

                @Override
                protected void onPostExecute(LoginWrapper loginWrapper) {
                    super.onPostExecute(loginWrapper);
                    if (isSuccess()) {
                        saveLoginInformation(loginWrapper, passwordStr);
                        startActivity(IntentHelper.openMain(context));
                        finish();
                    } else showError(throwable);
                }

                @Override
                protected FragmentManager getFragmentManager() {
                    return getSupportFragmentManager();
                }
            }.execute();
        }
    }

    private void showError(Throwable throwable) {
        ErrorAgent.reportError(throwable, "Login vk exception");
        showError(throwable instanceof InvocationTargetException ?
                throwable.getCause().getMessage() : throwable.getMessage());
    }

    private void showError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error);
        builder.setMessage(message);
        builder.show();
    }

    public void registerHandler(View view) {
        openRegisterActivity(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REGISTRATION_INTENT_CODE:
                if (resultCode == RESULT_OK) {
                    LoginWrapper loginObj = data.getParcelableExtra("login_info");
                    saveLoginInformation(loginObj);
                    startActivity(IntentHelper.openMain(this));
                    finish();
                }
                break;
            case RESET_PASSWORD_INTENT_CODE:
                if (resultCode == RESULT_OK) {
                    startActivity(IntentHelper.openMain(this));
                    finish();
                }
                break;
            case LOGIN_VK_CODE:
                if (resultCode == RESULT_OK) {
                    onTokenReceive(data.getStringExtra("token"));
                }
        }
    }

    private void onTokenReceive(final String token) {
        new DialogExceptionalAsyncTask<Void, Void, LoginWrapper>(this) {
            @Override
            protected LoginWrapper background(Void... params) throws Throwable {
                return vkLogin(new VkLoginRequest(token));
            }

            @Override
            protected void onPostExecute(LoginWrapper loginWrapper) {
                super.onPostExecute(loginWrapper);
                if (isSuccess()) {
                    saveLoginInformation(loginWrapper);
                    startActivity(IntentHelper.openMain(context));
                    finish();
                } else openRegisterActivity(token);
            }

            @Override
            protected FragmentManager getFragmentManager() {
                return getSupportFragmentManager();
            }
        }.execute();
    }

    private void openRegisterActivity(String vkToken) {
        startActivityForResult(new Intent(this, RegistrationActivity.class).putExtra(Config.VKONTAKTE_ID, vkToken), REGISTRATION_INTENT_CODE);
    }

    private void saveLoginInformation(LoginWrapper loginObj) {
        saveLoginInformation(loginObj, null);
    }

    private void saveLoginInformation(LoginWrapper loginObj, String password) {
        DataHelper.saveInfo(this, loginObj);
        if (password != null) {
            Storage.save(this, Config.PASSWORD, password);
        }

        GoogleCloudMessagingUtils cloudMessagingUtils = new BonusGCMUtils();
        cloudMessagingUtils.setOnGooglePlayServicesUnavailableListener(this);
        if (cloudMessagingUtils.checkPlayServices(this)) {
            cloudMessagingUtils.register(this);
        }
    }

    public void resetPasswordHandler(View view) {
        if (mailForm.validate()) {
            final String mailStr = mail.getText();
            final ResetPasswordRequest request = new ResetPasswordRequest(mailStr);
            new DialogExceptionalAsyncTask<Void, Void, SimpleResult>(this) {
                @Override
                protected SimpleResult background(Void... params) throws Throwable {
                    return resetPassword(request);
                }

                @Override
                protected void onPostExecute(SimpleResult simpleResult) {
                    super.onPostExecute(simpleResult);
                    if (isSuccess() && simpleResult.isSuccess()) {
                        startActivityForResult(new Intent(context, ResetPasswordActivity.class).putExtra("mail", mailStr),
                                RESET_PASSWORD_INTENT_CODE);
                    } else showError(throwable);
                }

                @Override
                protected FragmentManager getFragmentManager() {
                    return getSupportFragmentManager();
                }
            }.execute();
        }
    }

    public void enterVkHandler(View view) {
        VKSdk.authorize(VkMusicActivity.sMyScope, true, false);
    }

    @Override
    public void onGooglePlayServicesUnavailable(int resultCode) {
        Storage.save(this, Config.GOOGLE_SERVICES_RESULT_CODE, resultCode);
    }
}
