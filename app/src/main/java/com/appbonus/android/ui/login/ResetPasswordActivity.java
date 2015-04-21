package com.appbonus.android.ui.login;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.appbonus.android.R;
import com.appbonus.android.api.model.LoginRequest;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.model.api.LoginWrapper;
import com.appbonus.android.ui.ApiActivity;
import com.appbonus.android.ui.helper.DataHelper;
import com.dolphin.asynctask.DialogExceptionalAsyncTask;
import com.dolphin.net.exception.UnauthorizedException;

import java.lang.reflect.InvocationTargetException;

public class ResetPasswordActivity extends ApiActivity {
    protected String mail;
    protected FloatLabel password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_layout);
        mail = getIntent().getStringExtra("mail");
        password = (FloatLabel) findViewById(R.id.password);
    }

    public void resetPasswordHandler(View view) {
        String passwordStr = password.getText();
        final LoginRequest loginRequest = new LoginRequest(mail, passwordStr);
        new DialogExceptionalAsyncTask<Void, Void, LoginWrapper>(this) {

            @Override
            protected LoginWrapper background(Void... params) throws Throwable {
                return login(loginRequest);
            }

            @Override
            protected void onPostExecute(LoginWrapper loginWrapper) {
                super.onPostExecute(loginWrapper);
                if (isSuccess()) {
                    saveLoginInformation(loginWrapper);
                    setResult(RESULT_OK);
                    finish();
                } else showError(throwable);
            }

            @Override
            protected FragmentManager getFragmentManager() {
                return getSupportFragmentManager();
            }
        }.execute();
    }

    private void saveLoginInformation(LoginWrapper loginObj) {
        DataHelper.saveInfo(this, loginObj);
    }

    /*
     * 404 - мыло не зарегистрировано
     */
    private void showError(Throwable throwable) {
        if (UnauthorizedException.MESSAGE.equals(throwable.getMessage())) {
            showError(getString(R.string.wrong_login_or_password));
            return;
        }
        showError(throwable instanceof InvocationTargetException ?
                throwable.getCause().getMessage() : throwable.getMessage());
    }

    private void showError(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(s);
        builder.show();
    }
}
