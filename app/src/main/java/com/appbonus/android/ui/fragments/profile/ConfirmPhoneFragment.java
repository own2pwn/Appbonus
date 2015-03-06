package com.appbonus.android.ui.fragments.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appbonus.android.R;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.model.api.DataWrapper;
import com.appbonus.android.ui.fragments.common.OnTechSupportCallListener;
import com.dolphin.asynctask.DialogExceptionalAsyncTask;
import com.dolphin.ui.fragment.SimpleFragment;

public class ConfirmPhoneFragment extends SimpleFragment implements View.OnClickListener {
    protected FloatLabel code;
    protected Button confirmBtn;
    protected View techSupport;

    protected ConfirmPhoneFragmentListener listener;
    protected OnPhoneConfirmListener onPhoneConfirmListener;
    protected OnTechSupportCallListener onTechSupportCallListener;

    public interface ConfirmPhoneFragmentListener {
        DataWrapper confirmPhone(String code) throws Throwable;
    }

    public interface OnPhoneConfirmListener {
        void onPhoneConfirm();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ConfirmPhoneFragmentListener) activity;
        onTechSupportCallListener = (OnTechSupportCallListener) activity;
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
        onPhoneConfirmListener = (OnPhoneConfirmListener) getTargetFragment();
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
                onTechSupportCallListener.onTechSupportCall();
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
                return listener.confirmPhone(codeStr);
            }

            @Override
            protected void onPostExecute(DataWrapper dataWrapper) {
                super.onPostExecute(dataWrapper);
                if (dataWrapper != null && dataWrapper.isSuccess()) {
                    showToast(dataWrapper.toString());
                    if (onPhoneConfirmListener != null)
                        onPhoneConfirmListener.onPhoneConfirm();
                    closeCurrentFragment();

                } else showError(throwable.getMessage());
            }
        }.execute();
    }
}
