package com.appbonus.android.ui.fragments.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.component.DialogExceptionalAsyncTask;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.model.User;
import com.appbonus.android.model.api.DataWrapper;
import com.appbonus.android.model.api.UserWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.dolphin.activity.fragment.BaseFragment;
import com.dolphin.utils.KeyboardUtils;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.validate.ConfirmValidate;

public class ProfileEditorFragment extends BaseFragment implements View.OnClickListener {
    protected FloatLabel mail;
    protected FloatLabel phone;
    protected TextView country;

    protected FloatLabel newPassword;
    protected FloatLabel confirmPassword;

    protected Button saveBtn;
    protected Button changePasswordBtn;

    protected View confirmPhoneLabel;
    protected View confirmPhoneButton;

    protected Api api;
    protected User user;
    protected Form passwordForm;

    protected Fragment parentFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_editor, null);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = getTargetFragment();
        api = new ApiImpl(getActivity());
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCountry();
            }
        });
        Bundle bundle = takeArguments();
        user = (User) bundle.getSerializable("user");
        setData(user);
        setDrawerIndicatorEnabled(false);
    }

    public void changeCountry() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] countries = {
                getString(getResources().getIdentifier(User.COUNTRY_RUSSIA, "string", getActivity().getPackageName())),
                getString(getResources().getIdentifier(User.COUNTRY_BELARUS, "string", getActivity().getPackageName())),
                getString(getResources().getIdentifier(User.COUNTRY_UKRAINE, "string", getActivity().getPackageName()))};
        builder.setItems(countries,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        country.setText(countries[which]);
                        switch (which) {
                            case 0:
                                user.setCountry(User.COUNTRY_RUSSIA);
                                break;
                            case 1:
                                user.setCountry(User.COUNTRY_BELARUS);
                                break;
                            case 2:
                                user.setCountry(User.COUNTRY_UKRAINE);
                                break;
                        }
                    }
                });
        builder.show();
    }

    public void changePassword() {
        if (passwordForm.validate()) {

        }
    }

    public void save() {
        new DialogExceptionalAsyncTask<Void, Void, UserWrapper>(getActivity()) {
            @Override
            protected UserWrapper background(Void... params) throws Throwable {
                return api.writeProfile(getActivity(), SharedPreferencesStorage.getToken(context), getResult());
            }

            @Override
            protected void onPostExecute(UserWrapper userWrapper) {
                super.onPostExecute(userWrapper);
                if (isSuccess()) {
                    if (parentFragment instanceof OnUpdate)
                        ((OnUpdate) parentFragment).updateUser(userWrapper.getUser());
                    closeCurrentFragment();
                } else showError(throwable.getMessage());
            }

            @Override
            protected FragmentManager getFragmentManager() {
                return getActivity().getSupportFragmentManager();
            }
        }.execute();
    }

    private User getResult() {
        user.setEmail(mail.getText());
        user.setPhone(phone.getText());
        return user;
    }

    private void initUI(View view) {
        setTitle(R.string.profile);

        mail = (FloatLabel) view.findViewById(R.id.login);
        phone = (FloatLabel) view.findViewById(R.id.phone);
        country = (TextView) view.findViewById(R.id.country);
        newPassword = (FloatLabel) view.findViewById(R.id.new_password);
        confirmPassword = (FloatLabel) view.findViewById(R.id.confirm_password);
        saveBtn = (Button) view.findViewById(R.id.save);
        changePasswordBtn = (Button) view.findViewById(R.id.change_password);

        confirmPhoneLabel = view.findViewById(R.id.confirm_phone_label);
        confirmPhoneButton = view.findViewById(R.id.confirm_phone_button);
        confirmPhoneButton.setOnClickListener(this);

        passwordForm = new Form();
        ConfirmValidate confirmValidate = new ConfirmValidate(newPassword.getEditText(),
                confirmPassword.getEditText(), R.string.password_are_not_confirmed);
        passwordForm.addValidates(confirmValidate);
    }

    private void setData(User user) {
        mail.setText(user.getEmail());
        phone.setText(user.getPhone());
        country.setText(getString(getResources().getIdentifier(user.getCountry(), "string", getActivity().getPackageName())));

        if (!user.isPhoneConfirmed()) {
            confirmPhoneLabel.setVisibility(View.VISIBLE);
            confirmPhoneButton.setVisibility(View.VISIBLE);
        } else phone.lock();
    }

    @Override
    public void onClick(View v) {
        new DialogExceptionalAsyncTask<Void, Void, DataWrapper>(getActivity()) {
            @Override
            protected FragmentManager getFragmentManager() {
                return getActivity().getSupportFragmentManager();
            }

            @Override
            protected DataWrapper background(Void... params) throws Throwable {
                return api.confirmPhone(context);
            }

            @Override
            protected void onPostExecute(DataWrapper dataWrapper) {
                super.onPostExecute(dataWrapper);
                if (isSuccess()) {
                    if (dataWrapper.isSuccess())
                        Toast.makeText(context, dataWrapper.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }


    @Override
    public void onDestroyView() {
        KeyboardUtils.hideFragmentKeyboard(this);
        super.onDestroyView();
    }
}
