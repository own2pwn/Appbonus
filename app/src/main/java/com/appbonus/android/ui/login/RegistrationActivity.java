package com.appbonus.android.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.api.model.RegisterRequest;
import com.appbonus.android.api.model.VkLoginRequest;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.model.api.LoginWrapper;
import com.appbonus.android.storage.Config;
import com.appbonus.android.storage.Storage;
import com.dolphin.asynctask.DialogExceptionalAsyncTask;
import com.dolphin.net.exception.FormException;
import com.dolphin.utils.DeviceUtils;
import com.dolphin.utils.KeyboardUtils;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.throrinstudio.android.common.libs.validator.validator.PhoneValidator;

import org.apache.commons.lang3.StringUtils;

public class RegistrationActivity extends FragmentActivity {
    public static final int LOGIN_VK_CODE = 1;

    protected Api api;

    protected FloatLabel mail;
    protected FloatLabel phone;
    protected FloatLabel password;
    protected FloatLabel promo;

    protected Form form;
    protected Form vkForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        api = new ApiImpl(this);
        initUI();
        initValidators();
        KeyboardUtils.setupTouchEvents(this, getWindow().getDecorView());
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
    }

    public void registerHandler(View view) {
        closeErrors();
        if (form.validate()) {
            String mailStr = mail.getText();
            String phoneStr = phone.getText();
            String promoStr = promo.getText();
            final String passwordStr = password.getText();
            final RegisterRequest request = new RegisterRequest(mailStr, passwordStr, getCountry(),
                    phoneStr, DeviceUtils.getUniqueCode(this), promoStr);

            new DialogExceptionalAsyncTask<Void, Void, LoginWrapper>(this) {

                @Override
                protected LoginWrapper background(Void... params) throws Throwable {
                    return api.registration(request);
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
        builder.setTitle(throwable.getMessage());
        builder.show();

    }

    private void showFormException(FormException e) {
        if (StringUtils.equalsIgnoreCase(e.form, "email")) {
            mail.setError(e.message);
        } else if (StringUtils.equalsIgnoreCase(e.form, "phone")) {
            phone.setError(e.message);
        }
    }

    public void registerVkHandler(View view) {
        closeErrors();
        if (vkForm.validate()) {
            startActivityForResult(new Intent(this, LoginVkActivity.class), LOGIN_VK_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_VK_CODE:
                if (resultCode == RESULT_OK) {
                    final String token = data.getStringExtra("token");
                    final String mailStr = mail.getText();
                    final String phoneStr = phone.getText();
                    new DialogExceptionalAsyncTask<Void, Void, LoginWrapper>(this) {
                        @Override
                        protected LoginWrapper background(Void... params) throws Throwable {
                            return api.vkRegister(new VkLoginRequest(token, mailStr, phoneStr));
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
                break;
        }
    }

    public void closeErrors() {
        form.closeAllErrors();
        vkForm.closeAllErrors();
    }
}
