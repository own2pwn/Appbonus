package com.appbonus.android.ui.fragments.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appbonus.android.R;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.model.User;
import com.appbonus.android.model.api.UserWrapper;
import com.appbonus.android.model.enums.Sex;
import com.appbonus.android.storage.Config;
import com.appbonus.android.storage.Storage;
import com.appbonus.android.ui.fragments.profile.settings.SettingsFragment;
import com.dolphin.ui.LoadingDialogHelper;
import com.dolphin.ui.fragment.root.RootSimpleFragment;
import com.dolphin.utils.KeyboardUtils;

import java.text.SimpleDateFormat;

public class ProfileBrowserFragment extends RootSimpleFragment implements LoaderManager.LoaderCallbacks<UserWrapper>,
        OnUserUpdateListener, View.OnClickListener {
    public static final int LOADER_ID = 1;

    protected FloatLabel mail;
    protected FloatLabel phone;
    protected FloatLabel name;
    protected FloatLabel birthDate;
    protected TextView sex;

    protected FloatLabel newPassword;
    protected FloatLabel confirmPassword;

    protected Button editBtn;

    protected View confirmPhoneLabel;

    protected User user;

    protected ProfileBrowserFragmentListener listener;

    public interface ProfileBrowserFragmentListener extends LoadingDialogHelper {
        Loader<UserWrapper> createUserLoader();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ProfileBrowserFragmentListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_browse, null);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().restartLoader(LOADER_ID, null, this);
        setDrawerIndicatorEnabled(getTargetFragment() == null);
        KeyboardUtils.hideFragmentKeyboard(this);
    }

    private void initUI(View view) {
        setTitle(R.string.profile);

        mail = (FloatLabel) view.findViewById(R.id.login);
        phone = (FloatLabel) view.findViewById(R.id.phone);
        name = (FloatLabel) view.findViewById(R.id.name);
        birthDate = (FloatLabel) view.findViewById(R.id.birthdate);
        sex = (TextView) view.findViewById(R.id.sex);

        newPassword = (FloatLabel) view.findViewById(R.id.new_password);
        confirmPassword = (FloatLabel) view.findViewById(R.id.confirm_password);

        editBtn = (Button) view.findViewById(R.id.edit);

        confirmPhoneLabel = view.findViewById(R.id.confirm_phone_label);

        editBtn.setOnClickListener(this);
    }

    private void setData(User user) {
        mail.setText(user.getEmail());
        phone.setText(user.getPhone());
        name.setText(user.getName());
        if (user.getBirthDate() != null) {
            birthDate.setText(new SimpleDateFormat(getString(R.string.profile_date_format)).format(user.getBirthDate()));
        }
        if (user.getGender() == Sex.MALE) {
            sex.setText(R.string.sex_male);
        } else if (user.getGender() == Sex.FEMALE) {
            sex.setText(R.string.sex_female);
        }


        if (!user.isPhoneConfirmed()) {
            confirmPhoneLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<UserWrapper> onCreateLoader(int id, Bundle args) {
        Loader<UserWrapper> loader = listener.createUserLoader();
        loader.forceLoad();
        listener.showLoadingDialog();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<UserWrapper> loader, UserWrapper data) {
        if (data != null) {
            user = data.getUser();
            setData(user);
        }
        listener.dismissLoadingDialog();
    }

    @Override
    public void onLoaderReset(Loader<UserWrapper> loader) {
        listener.dismissLoadingDialog();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_settings:
                placeProperFragment(SettingsFragment.class.getName(), getUserBundle());
                return true;
            case android.R.id.home:
                if (getTargetFragment() == null) {
                    toggleDrawer();
                    return true;
                } else return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdate(User user) {
        Storage.save(getActivity(), Config.USER, user);
        this.user = user;
    }

    @Override
    public void onClick(View v) {
        if (user != null) {
            placeProperFragment(ProfileEditorFragment.class.getName(), getUserBundle());
        } else Toast.makeText(getActivity(), R.string.re_enter, Toast.LENGTH_LONG).show();
    }

    private Bundle getUserBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        return bundle;
    }
}
