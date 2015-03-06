package com.appbonus.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.sebbia.ActiveAndroid;
import com.activeandroid.sebbia.query.Delete;
import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.model.Question;
import com.appbonus.android.model.api.QuestionsWrapper;
import com.appbonus.android.storage.Config;
import com.appbonus.android.storage.Storage;
import com.appbonus.android.ui.helper.IntentHelper;
import com.appbonus.android.ui.login.LoginActivity;
import com.dolphin.asynctask.ExceptionAsyncTask;

import java.util.List;

public class SplashActivity extends Activity implements View.OnClickListener {
    public static final long MILLS_PER_SECOND = 1000;
    public static final long SPLASH_DELAY = MILLS_PER_SECOND * 1;

    protected Api api;
    protected TextView loadingText;
    protected View progressBar;
    protected View rootView;
    protected boolean loadingIsExecuted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        api = new ApiImpl(this);
        loadingText = (TextView) findViewById(R.id.loading_text);
        progressBar = findViewById(R.id.progress_circular);
        rootView = findViewById(R.id.root_view);
        loadingIsExecuted = false;

        loading();
    }

    private void loading() {
        if (!loadingIsExecuted) {
            new ExceptionAsyncTask<Void, Void, QuestionsWrapper>(this) {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    rootView.setOnClickListener(null);
                    progressBar.setVisibility(View.VISIBLE);
                    loadingText.setText(R.string.loading);
                }

                @Override
                protected QuestionsWrapper background(Void... params) throws Throwable {
                    return api.getFaq();
                }

                @Override
                protected void onPostExecute(QuestionsWrapper questionsWrapper) {
                    super.onPostExecute(questionsWrapper);
                    if (isSuccess()) {
                        saveFaq(questionsWrapper);
                        enter();
                    } else {
                        showNetworkError();
                    }
                }
            }.execute();
            loadingIsExecuted = true;
        }
    }

    private void showNetworkError() {
        progressBar.setVisibility(View.INVISIBLE);
        loadingText.setText(R.string.network_problem);
        rootView.setOnClickListener(this);
        loadingIsExecuted = false;
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
                if (TextUtils.isEmpty(Storage.<CharSequence>load(SplashActivity.this, Config.TOKEN))) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else startActivity(IntentHelper.openMain(SplashActivity.this));
                finish();
            }
        }, SPLASH_DELAY);
    }

    @Override
    public void onClick(View v) {
        loading();
    }
}
