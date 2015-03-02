package com.appbonus.android.ui.fragments.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appbonus.android.R;
import com.appbonus.android.api.model.ChangePasswordRequest;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.model.User;
import com.appbonus.android.model.api.DataWrapper;
import com.appbonus.android.model.api.UserWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.appbonus.android.ui.LoadingDialogHelper;
import com.dolphin.asynctask.DialogExceptionalAsyncTask;
import com.dolphin.ui.fragment.SimpleFragment;
import com.dolphin.utils.KeyboardUtils;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validate.ConfirmValidate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

public class ProfileEditorFragment extends SimpleFragment implements View.OnClickListener {
    protected FloatLabel mail;
    protected FloatLabel phone;
    protected TextView country;

    protected FloatLabel newPassword;
    protected FloatLabel confirmPassword;

    protected Button saveBtn;
    protected Button changePasswordBtn;

    protected View confirmPhoneLabel;
    protected View confirmPhoneButton;

    protected User user;
    protected Form passwordForm;
    protected Form mailForm;

    protected Fragment parentFragment;

    protected ProfileEditorFragmentListener listener;

    public interface ProfileEditorFragmentListener extends LoadingDialogHelper {
        UserWrapper changePassword(ChangePasswordRequest request) throws Throwable;

        UserWrapper writeProfile(User request) throws Throwable;

        DataWrapper requestConfirmation() throws Throwable;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ProfileEditorFragmentListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentFragment = getTargetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_editor, null);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");
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
            final String password = newPassword.getText();
            final ChangePasswordRequest request = new ChangePasswordRequest(SharedPreferencesStorage.getToken(getActivity()),
                    SharedPreferencesStorage.getPassword(getActivity()), password);
            new DialogExceptionalAsyncTask<Void, Void, UserWrapper>(getActivity()) {
                @Override
                protected FragmentManager getFragmentManager() {
                    return getActivity().getSupportFragmentManager();
                }

                @Override
                protected UserWrapper background(Void... params) throws Throwable {
                    return listener.changePassword(request);
                }

                @Override
                protected void onPostExecute(UserWrapper userWrapper) {
                    super.onPostExecute(userWrapper);
                    if (isSuccess()) {
                        SharedPreferencesStorage.savePassword(context, password);
                        newPassword.clear();
                        confirmPassword.clear();
                        Toast.makeText(context, R.string.password_was_changed, Toast.LENGTH_LONG).show();
                    } else showError(throwable.getMessage());
                }
            }.execute();
        }
    }

    public void save() {
        if (mailForm.validate()) {
            new DialogExceptionalAsyncTask<Void, Void, UserWrapper>(getActivity()) {
                @Override
                protected UserWrapper background(Void... params) throws Throwable {
                    return listener.writeProfile(getResult());
                }

                @Override
                protected void onPostExecute(UserWrapper userWrapper) {
                    super.onPostExecute(userWrapper);
                    if (isSuccess()) {
                        if (parentFragment instanceof OnUserUpdateListener)
                            ((OnUserUpdateListener) parentFragment).onUpdate(userWrapper.getUser());
                        closeCurrentFragment();
                    } else showError(throwable.getMessage());
                }

                @Override
                protected FragmentManager getFragmentManager() {
                    return getActivity().getSupportFragmentManager();
                }
            }.execute();
        }
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
        saveBtn.setOnClickListener(this);
        changePasswordBtn.setOnClickListener(this);
        country.setOnClickListener(this);

        confirmPhoneLabel = view.findViewById(R.id.confirm_phone_label);
        confirmPhoneButton = view.findViewById(R.id.confirm_phone_button);
        confirmPhoneButton.setOnClickListener(this);

        passwordForm = new Form();

        NotEmptyValidator newPasswordValidator = new NotEmptyValidator(getActivity(), R.string.input_password);
        Validate newPasswordValidate = new Validate(newPassword.getEditText());
        newPasswordValidate.addValidator(newPasswordValidator);

        NotEmptyValidator confirmPasswordValidator = new NotEmptyValidator(getActivity(), R.string.input_password);
        Validate confirmPasswordValidate = new Validate(confirmPassword.getEditText());
        confirmPasswordValidate.addValidator(confirmPasswordValidator);

        ConfirmValidate confirmValidate = new ConfirmValidate(newPassword.getEditText(),
                confirmPassword.getEditText(), R.string.password_are_not_confirmed);
        passwordForm.addValidates(confirmValidate);
        passwordForm.addValidates(newPasswordValidate);
        passwordForm.addValidates(confirmPasswordValidate);

        mailForm = new Form();
        Validate mailValidate = new Validate(mail.getEditText());
        mailValidate.addValidator(new EmailValidator(getActivity(), R.string.wrong_mail));
        mailForm.addValidates(mailValidate);
    }

    private void setData(User user) {
        mail.setText(user.getEmail());
        phone.setText(user.getPhone());
        if (!TextUtils.isEmpty(user.getCountry())) {
            country.setText(getString(getResources().getIdentifier(user.getCountry(), "string", getActivity().getPackageName())));
        }

        setPhoneSetting(user);
    }

    private void setPhoneSetting(User user) {
        if (!user.isPhoneConfirmed()) {
            confirmPhoneLabel.setVisibility(View.VISIBLE);
            confirmPhoneButton.setVisibility(View.VISIBLE);
        } else {
            confirmPhoneLabel.setVisibility(View.GONE);
            confirmPhoneButton.setVisibility(View.GONE);
            phone.lock();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.save:
                save();
                break;
            case R.id.change_password:
                changePassword();
                break;
            case R.id.confirm_phone_button:
                confirmPhone();
                break;
            case R.id.country:
                changeCountry();
                break;
        }
    }

    private void confirmPhone() {
        new DialogExceptionalAsyncTask<Void, Void, DataWrapper>(getActivity()) {
            @Override
            protected FragmentManager getFragmentManager() {
                return getActivity().getSupportFragmentManager();
            }

            @Override
            protected DataWrapper background(Void... params) throws Throwable {
                return listener.requestConfirmation();
            }

            @Override
            protected void onPostExecute(DataWrapper dataWrapper) {
                super.onPostExecute(dataWrapper);
                if (isSuccess()) {
                    Toast.makeText(context, dataWrapper.toString(), Toast.LENGTH_LONG).show();
                    placeProperFragment(ConfirmPhoneFragment.class.getName());
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
