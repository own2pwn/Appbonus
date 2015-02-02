package com.appbonus.android.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.component.DialogExceptionalAsyncTask;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.model.api.LoginWrapper;
import com.dolphin.utils.DeviceUtils;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.throrinstudio.android.common.libs.validator.validator.PhoneValidator;

public class RegistrationActivity extends FragmentActivity {
    public static final int LOGIN_VK_CODE = 1;

    protected Api api;

    protected FloatLabel mail;
    protected FloatLabel phone;
    protected FloatLabel password;

    protected Form form;
    protected Form mailForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        api = new ApiImpl(this);
        initUI();
        initValidators();
    }

    public void initValidators() {
        form = new Form();
        mailForm = new Form();

        Validate mailValidate = new Validate(mail.getEditText());
        mailValidate.addValidator(new EmailValidator(this, R.string.wrong_mail));
        mailValidate.addValidator(new NotEmptyValidator(this, R.string.input_mail));

        Validate phoneValidate = new Validate(phone.getEditText());
        phoneValidate.addValidator(new PhoneValidator(this, R.string.wrong_phone));

        Validate passwordValidate = new Validate(password.getEditText());
        passwordValidate.addValidator(new NotEmptyValidator(this, R.string.input_password));

        form.addValidates(mailValidate);
        form.addValidates(phoneValidate);
        form.addValidates(passwordValidate);

        mailForm.addValidates(mailValidate);
    }

    public void initUI() {
        mail = (FloatLabel) findViewById(R.id.login);
        phone = (FloatLabel) findViewById(R.id.phone);
        password = (FloatLabel) findViewById(R.id.password);
    }

    public void registerHandler(View view) {
        closeErrors();
        if (form.validate()) {
            final String mailStr = mail.getText();
            final String phoneStr = phone.getText();
            final String passwordStr = password.getText();

            new DialogExceptionalAsyncTask<Void, Void, LoginWrapper>(this) {

                @Override
                protected LoginWrapper background(Void... params) throws Throwable {
                    return api.registration(mailStr, passwordStr, getCountry(), phoneStr, DeviceUtils.getUniqueCode(context));
                }

                @Override
                protected void onPostExecute(LoginWrapper loginWrapper) {
                    super.onPostExecute(loginWrapper);
                    if (isSuccess()) {
                        setResult(RESULT_OK, new Intent().putExtra("login_info", loginWrapper));
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

    private String getCountry() {
        return "russia";
    }

    /*
     * 422 - такое мыло уже используется или мыло введено некорректно или мыло вообще не введено
     */
    private void showError(Throwable throwable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(throwable.getMessage());
        builder.show();
    }

    public void registerVkHandler(View view) {
        closeErrors();
        if (mailForm.validate()) {
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
                    new DialogExceptionalAsyncTask<Void, Void, LoginWrapper>(this) {
                        @Override
                        protected LoginWrapper background(Void... params) throws Throwable {
                            return api.vkRegister(context, mailStr, token);
                        }

                        @Override
                        protected void onPostExecute(LoginWrapper loginWrapper) {
                            super.onPostExecute(loginWrapper);
                            if (isSuccess()) {
                                setResult(RESULT_OK, new Intent().putExtra("login_info", loginWrapper));
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
        mailForm.closeAllErrors();
    }
}