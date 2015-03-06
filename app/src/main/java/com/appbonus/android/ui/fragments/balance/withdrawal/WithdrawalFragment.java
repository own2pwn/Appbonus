package com.appbonus.android.ui.fragments.balance.withdrawal;

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
import com.appbonus.android.model.WithdrawalRequest;
import com.appbonus.android.model.api.DataWrapper;
import com.appbonus.android.ui.fragments.balance.OnWithdrawalListener;
import com.dolphin.asynctask.DialogExceptionalAsyncTask;
import com.dolphin.ui.fragment.SimpleFragment;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

public class WithdrawalFragment extends SimpleFragment implements View.OnClickListener {
    protected Button mobileBtn;
    protected Button qiwiBtn;

    protected FloatLabel amount;
    protected FloatLabel mobileNumber;

    protected Form form;

    protected WithdrawalFragmentListener listener;
    protected OnWithdrawalListener onWithdrawalListener;

    public interface WithdrawalFragmentListener {
        DataWrapper makeWithdrawal(WithdrawalRequest request) throws Throwable;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (WithdrawalFragmentListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.withdrawal_layout, null);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.withdrawal_title);
        setDrawerIndicatorEnabled(false);
        onWithdrawalListener = (OnWithdrawalListener) getTargetFragment();
    }

    private void initUI(View view) {
        mobileBtn = (Button) view.findViewById(R.id.withdrawal_phone);
        qiwiBtn = (Button) view.findViewById(R.id.withdrawal_qiwi);

        amount = (FloatLabel) view.findViewById(R.id.amount);
        mobileNumber = (FloatLabel) view.findViewById(R.id.phone);

        mobileBtn.setOnClickListener(this);
        qiwiBtn.setOnClickListener(this);

        form = new Form();
        Validate amountValidate = new Validate(amount.getEditText());
        NotEmptyValidator amountValidator = new NotEmptyValidator(getActivity(), R.string.empty_amount);
        amountValidate.addValidator(amountValidator);
        form.addValidates(amountValidate);
    }

    @Override
    public void onClick(View v) {
        if (form.validate()) {
            int id = v.getId();
            WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
            withdrawalRequest.setAmount(Double.parseDouble(amount.getText()));
            switch (id) {
                case R.id.withdrawal_qiwi:
                    withdrawalRequest.setRequestType(WithdrawalRequest.QIWI_REQUEST_TYPE);
                    break;
                case R.id.withdrawal_phone:
                    withdrawalRequest.setRequestType(WithdrawalRequest.QIWI_REQUEST_TYPE);
                    break;
            }
            makeWithdrawal(withdrawalRequest);
        }
    }

    private void makeWithdrawal(final WithdrawalRequest withdrawalRequest) {
        new DialogExceptionalAsyncTask<Void, Void, DataWrapper>(getActivity()) {
            @Override
            protected FragmentManager getFragmentManager() {
                return getActivity().getSupportFragmentManager();
            }

            @Override
            protected DataWrapper background(Void... params) throws Throwable {
                return listener.makeWithdrawal(withdrawalRequest);
            }

            @Override
            protected void onPostExecute(DataWrapper dataWrapper) {
                super.onPostExecute(dataWrapper);
                if (isSuccess()) {
                    notifyObservers();
                    closeCurrentFragment();
                } else showError(throwable.getMessage());
            }
        }.execute();
    }

    private void notifyObservers() {
        if (onWithdrawalListener != null) {
            onWithdrawalListener.onWithdrawal();
        }
    }
}
