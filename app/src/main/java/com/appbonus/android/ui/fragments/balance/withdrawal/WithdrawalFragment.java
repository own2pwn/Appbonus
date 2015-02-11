package com.appbonus.android.ui.fragments.balance.withdrawal;

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
import com.appbonus.android.component.DialogExceptionalAsyncTask;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.model.WithdrawalRequest;
import com.appbonus.android.model.api.DataWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.dolphin.ui.fragment.SimpleFragment;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

public class WithdrawalFragment extends SimpleFragment implements View.OnClickListener {
    protected Api api;

    protected Button mobileBtn;
    protected Button qiwiBtn;

    protected FloatLabel amount;
    protected FloatLabel mobileNumber;

    protected Form form;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new ApiImpl(getActivity());
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
                return api.makeWithdrawal(new com.appbonus.android.api.model.WithdrawalRequest(
                        SharedPreferencesStorage.getToken(context), withdrawalRequest));
            }

            @Override
            protected void onPostExecute(DataWrapper dataWrapper) {
                super.onPostExecute(dataWrapper);
                if (isSuccess()) {
                    closeCurrentFragment();
                } else showError(throwable.getMessage());
            }
        }.execute();
    }
}
