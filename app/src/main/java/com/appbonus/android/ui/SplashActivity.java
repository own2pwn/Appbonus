package com.appbonus.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.activeandroid.sebbia.ActiveAndroid;
import com.activeandroid.sebbia.query.Delete;
import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.model.Question;
import com.appbonus.android.model.api.QuestionsWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.appbonus.android.ui.helper.IntentHelper;
import com.appbonus.android.ui.login.LoginActivity;
import com.dolphin.asynctask.ExceptionAsyncTask;

import java.util.List;

public class SplashActivity extends Activity {
    public static final long MILLS_PER_SECOND = 1000;
    public static final long SPLASH_DELAY = MILLS_PER_SECOND * 1;

    protected Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        api = new ApiImpl(this);

        new ExceptionAsyncTask<Void, Void, QuestionsWrapper>(this) {
            @Override
            protected QuestionsWrapper background(Void... params) throws Throwable {
                return api.getFaq();
            }

            @Override
            protected void onPostExecute(QuestionsWrapper questionsWrapper) {
                super.onPostExecute(questionsWrapper);
                saveFaq(questionsWrapper);
                enter();
            }
        }.execute();
    }

    private void saveFaq(QuestionsWrapper questionsWrapper) {
        if (questionsWrapper != null) {
            List<Question> questions = questionsWrapper.getQuestions();
            if (questions != null) {
                new Delete().from(Question.class).execute();
                ActiveAndroid.beginTransaction();
                try {
                    for (Question question : questions) {
                        question.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } catch (Throwable ignored) {
                } finally {
                    ActiveAndroid.endTransaction();
                }
            }
        }
    }

    private void enter() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(SharedPreferencesStorage.getToken(SplashActivity.this))) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else startActivity(IntentHelper.openMain(SplashActivity.this));
                finish();
            }
        }, SPLASH_DELAY);
    }
}
