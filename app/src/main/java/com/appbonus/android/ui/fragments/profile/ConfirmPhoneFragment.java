package com.appbonus.android.ui.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.api.model.ConfirmPhoneRequest;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.model.api.DataWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.dolphin.asynctask.DialogExceptionalAsyncTask;
import com.dolphin.helper.IntentHelper;
import com.dolphin.ui.fragment.SimpleFragment;

public class ConfirmPhoneFragment extends SimpleFragment implements View.OnClickListener {
    protected Api api;
    protected FloatLabel code;
    protected Button confirmBtn;
    protected View techSupport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new ApiImpl(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phone_code_layout, null);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDrawerIndicatorEnabled(false);
        setTitle(R.string.confirm_title);
    }

    private void initUI(View view) {
        code = (FloatLabel) view.findViewById(R.id.code);
        confirmBtn = (Button) view.findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(this);

        techSupport = view.findViewById(R.id.tech_support);
        techSupport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.confirm:
                confirm();
                break;
            case R.id.tech_support:
                startActivity(IntentHelper.sendTextEmail(null, null, new String[]{getString(R.string.app_bonus_mail)}));
                break;
        }
    }

    private void confirm() {
        final String codeStr = code.getText();
        new DialogExceptionalAsyncTask<Void, Void, DataWrapper>(getActivity()) {
            @Override
            protected FragmentManager getFragmentManager() {
                return getActivity().getSupportFragmentManager();
            }

            @Override
            protected DataWrapper background(Void... params) throws Throwable {
                return api.confirmPhone(new ConfirmPhoneRequest(SharedPreferencesStorage.getToken(context), codeStr));
            }

            @Override
            protected void onPostExecute(DataWrapper dataWrapper) {
                super.onPostExecute(dataWrapper);
            }
        }.execute();
    }
}