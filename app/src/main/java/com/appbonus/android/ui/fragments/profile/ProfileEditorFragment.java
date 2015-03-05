package com.appbonus.android.ui.fragments.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.appbonus.android.R;
import com.appbonus.android.api.model.ChangePasswordRequest;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.model.User;
import com.appbonus.android.model.api.DataWrapper;
import com.appbonus.android.model.api.UserWrapper;
import com.appbonus.android.model.enums.Sex;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.dolphin.asynctask.DialogExceptionalAsyncTask;
import com.dolphin.component.DatePickerDialog;
import com.dolphin.ui.LoadingDialogHelper;
import com.dolphin.ui.fragment.SimpleFragment;
import com.dolphin.utils.KeyboardUtils;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validate.ConfirmValidate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProfileEditorFragment extends SimpleFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        ConfirmPhoneFragment.OnPhoneConfirmListener {
    public static final String SEX_NAME_PARAMETER = "name";
    protected FloatLabel mail;
    protected FloatLabel phone;
    protected FloatLabel name;
    protected FloatLabel birthDate;
    protected Spinner sex;
    protected SimpleAdapter sexAdapter;

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                user.setGender(null);
                break;
            case 1:
                //male
                user.setGender(Sex.MALE);
                break;
            case 2:
                //female
                user.setGender(Sex.FEMALE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onPhoneConfirm() {
        user.setPhoneConfirmed(true);
    }

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
        user.setName(name.getText());
        return user;
    }

    private void initUI(View view) {
        setTitle(R.string.profile);

        mail = (FloatLabel) view.findViewById(R.id.login);
        phone = (FloatLabel) view.findViewById(R.id.phone);
        name = (FloatLabel) view.findViewById(R.id.name);
        birthDate = (FloatLabel) view.findViewById(R.id.birthdate);
        birthDate.setOnClickListener(this);
        sex = (Spinner) view.findViewById(R.id.sex);
        initSexSpinner();

        newPassword = (FloatLabel) view.findViewById(R.id.new_password);
        confirmPassword = (FloatLabel) view.findViewById(R.id.confirm_password);
        saveBtn = (Button) view.findViewById(R.id.save);
        changePasswordBtn = (Button) view.findViewById(R.id.change_password);
        saveBtn.setOnClickListener(this);
        changePasswordBtn.setOnClickListener(this);

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

    private void initSexSpinner() {
        sexAdapter = new SimpleAdapter(getActivity(), sexData(), R.layout.simple_item, new String[]{SEX_NAME_PARAMETER}, new int[]{android.R.id.text1});
        sexAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        sex.setAdapter(sexAdapter);
        sex.setOnItemSelectedListener(this);
    }

    private List<Map<String, String>> sexData() {
        List<Map<String, String>> data = new ArrayList<>(3);
        data.add(Collections.singletonMap(SEX_NAME_PARAMETER, ""));
        data.add(Collections.singletonMap(SEX_NAME_PARAMETER, getString(R.string.sex_male)));
        data.add(Collections.singletonMap(SEX_NAME_PARAMETER, getString(R.string.sex_female)));
        return data;
    }

    private void setData(User user) {
        mail.setText(user.getEmail());
        phone.setText(user.getPhone());
        name.setText(user.getName());
        if (user.getBirthDate() != null) {
            birthDate.setText(new SimpleDateFormat(getString(R.string.profile_date_format)).format(user.getBirthDate()));
        }
        if (user.getGender() != null)
            sex.setSelection(user.getGender().ordinal());

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
            case R.id.edit_text:
                openBirthDateDialog();
                break;
        }
    }

    private void openBirthDateDialog() {
        DatePickerDialog.newInstance(user.getBirthDate(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(Date date) {
                user.setBirthDate(date);
                birthDate.setText(new SimpleDateFormat(getString(R.string.profile_date_format)).format(user.getBirthDate()));
            }
        }).show(getFragmentManager(), "birth_date_dialog");
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
