package com.appbonus.android.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.View;

import com.appbonus.android.R;
import com.appbonus.android.api.model.RegisterRequest;
import com.appbonus.android.api.model.VkLoginRequest;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.model.api.LoginWrapper;
import com.appbonus.android.storage.Config;
import com.appbonus.android.storage.Storage;
import com.appbonus.android.ui.ApiActivity;
import com.appbonus.android.ui.BonusVkSdkListener;
import com.appbonus.android.ui.VkActivity;
import com.dolphin.asynctask.DialogExceptionalAsyncTask;
import com.dolphin.net.exception.FormException;
import com.dolphin.utils.DeviceUtils;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.throrinstudio.android.common.libs.validator.validator.PhoneValidator;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;

import org.apache.commons.lang3.StringUtils;

public class RegistrationActivity extends ApiActivity {
    public static final int LOGIN_VK_CODE = 1;

    protected FloatLabel mail;
    protected FloatLabel phone;
    protected FloatLabel password;
    protected FloatLabel promo;
    protected View vkontakteView;

    protected Form form;
    protected Form vkForm;

    protected String vkToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vkToken = getIntent().getExtras().getString(Config.VKONTAKTE_ID);
        setContentView(R.layout.registration_layout);
        initUI();
        initValidators();
    }

    private VKSdkListener sdkListener = new BonusVkSdkListener() {
        @Override
        protected void onToken(String token) {
            if (!isFinishing())
                onTokenReceive(token);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        VKSdk.initialize(sdkListener, getString(R.string.vk_id));
    }

    private void onTokenReceive(String token) {
        String mailStr = mail.getText();
        String phoneStr = phone.getText();
        String promoStr = promo.getText();
        vkRegister(token, mailStr, phoneStr, promoStr);
    }

    public void initValidators() {
        form = new Form();
        vkForm = new Form();

        Validate mailValidate = new Validate(mail.getEditText());
        mailValidate.addValidator(new EmailValidator(this, R.string.wrong_mail));
        mailValidate.addValidator(new NotEmptyValidator(this, R.string.input_mail));

        Validate phoneValidate = new Validate(phone.getEditText());
        phoneValidate.addValidator(new PhoneValidator(this, R.string.wrong_phone));
        phoneValidate.addValidator(new NotEmptyValidator(this, R.string.input_phone));

        Validate passwordValidate = new Validate(password.getEditText());
        passwordValidate.addValidator(new NotEmptyValidator(this, R.string.input_password));

        form.addValidates(mailValidate);
        form.addValidates(phoneValidate);
        form.addValidates(passwordValidate);

        vkForm.addValidates(mailValidate);
        vkForm.addValidates(phoneValidate);
    }

    public void initUI() {
        mail = (FloatLabel) findViewById(R.id.login);
        phone = (FloatLabel) findViewById(R.id.phone);
        password = (FloatLabel) findViewById(R.id.password);
        promo = (FloatLabel) findViewById(R.id.promo);
        vkontakteView = findViewById(R.id.vkontakte);
        vkontakteView.setVisibility(TextUtils.isEmpty(vkToken) ? View.VISIBLE : View.GONE);

        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    public void registerHandler(View view) {
        closeErrors();
        if (form.validate()) {
            String mailStr = mail.getText();
            String phoneStr = phone.getText();
            String promoStr = promo.getText();
            String passwordStr = password.getText();

            if (TextUtils.isEmpty(vkToken))
                emailRegister(mailStr, phoneStr, promoStr, passwordStr);
            else vkRegister(vkToken, mailStr, phoneStr, promoStr);
        }
    }

    protected void emailRegister(String mailStr, String phoneStr, String promoStr, final String passwordStr) {
        final RegisterRequest request = new RegisterRequest(mailStr, passwordStr, getCountry(),
                phoneStr, DeviceUtils.getUniqueCode(this), promoStr);

        new DialogExceptionalAsyncTask<Void, Void, LoginWrapper>(this) {

            @Override
            protected LoginWrapper background(Void... params) throws Throwable {
                return registration(request);
            }

            @Override
            protected void onPostExecute(LoginWrapper loginWrapper) {
                super.onPostExecute(loginWrapper);
                if (isSuccess()) {
                    savePassword(passwordStr);
                    setResult(RESULT_OK, new Intent().putExtra("login_info", ((Parcelable) loginWrapper)));
                    finish();
                } else showError(throwable);
            }

            @Override
            protected FragmentManager getFragmentManager() {
                return getSupportFragmentManager();
            }
        }.execute();
    }

    private void savePassword(String passwordStr) {
        Storage.save(this, Config.PASSWORD, passwordStr);
    }

    private String getCountry() {
        return "russia";
    }

    /*
     * 422 - такое мыло уже используется или мыло введено некорректно или мыло вообще не введено
     */
    private void showError(Throwable throwable) {
        if (throwable instanceof FormException) {
            showFormException(((FormException) throwable));
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error);
        builder.setMessage(throwable.getMessage());
        builder.show();
    }

    private void showFormException(FormException e) {
        if (StringUtils.equalsIgnoreCase(e.form, "email")) {
            mail.setError(e.message);
        } else if (StringUtils.equalsIgnoreCase(e.form, "phone")) {
            phone.setError(e.message);
        } else showError(new Throwable(e.message));
    }

    public void registerVkHandler(View view) {
        closeErrors();
        if (vkForm.validate()) {
            VKSdk.authorize(VkActivity.scope, true, false);
        }
    }

    private void vkRegister(final String token, final String mailStr, final String phoneStr, final String promoStr) {
        new DialogExceptionalAsyncTask<Void, Void, LoginWrapper>(this) {
            @Override
            protected LoginWrapper background(Void... params) throws Throwable {
                return vkRegistration(new VkLoginRequest(token, mailStr, phoneStr, promoStr));
            }

            @Override
            protected void onPostExecute(LoginWrapper loginWrapper) {
                super.onPostExecute(loginWrapper);
                if (isSuccess()) {
                    setResult(RESULT_OK, new Intent().putExtra("login_info", ((Parcelable) loginWrapper)));
                    finish();
                } else showError(throwable);
            }

            @Override
            protected FragmentManager getFragmentManager() {
                return getSupportFragmentManager();
            }
        }.execute();
    }

    public void closeErrors() {
        form.closeAllErrors();
        vkForm.closeAllErrors();
    }
}
