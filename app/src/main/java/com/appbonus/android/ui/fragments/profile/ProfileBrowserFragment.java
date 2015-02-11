package com.appbonus.android.ui.fragments.profile;

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
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.loaders.ProfileLoader;
import com.appbonus.android.model.User;
import com.appbonus.android.model.api.UserWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.appbonus.android.ui.fragments.profile.settings.SettingsFragment;
import com.dolphin.ui.fragment.root.RootBaseFragment;
import com.dolphin.utils.KeyboardUtils;

public class ProfileBrowserFragment extends RootBaseFragment implements LoaderManager.LoaderCallbacks<UserWrapper>,
        OnUserUpdateListener, View.OnClickListener {
    public static final int LOADER_ID = 1;

    protected FloatLabel mail;
    protected FloatLabel phone;
    protected TextView country;

    protected FloatLabel newPassword;
    protected FloatLabel confirmPassword;

    protected Button editBtn;

    protected View confirmPhoneLabel;

    protected Api api;
    protected User user;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_browse, null);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        api = new ApiImpl(getActivity());
        if (getLoaderManager().getLoader(LOADER_ID) == null) {
            Loader<UserWrapper> loader = getLoaderManager().initLoader(LOADER_ID, null, this);
            loader.forceLoad();
        }
        setDrawerIndicatorEnabled(getTargetFragment() == null);
        KeyboardUtils.hideFragmentKeyboard(this);
    }

    private void initUI(View view) {
        setTitle(R.string.profile);

        mail = (FloatLabel) view.findViewById(R.id.login);
        phone = (FloatLabel) view.findViewById(R.id.phone);
        country = (TextView) view.findViewById(R.id.country);
        newPassword = (FloatLabel) view.findViewById(R.id.new_password);
        confirmPassword = (FloatLabel) view.findViewById(R.id.confirm_password);
        editBtn = (Button) view.findViewById(R.id.edit);

        confirmPhoneLabel = view.findViewById(R.id.confirm_phone_label);

        editBtn.setOnClickListener(this);
    }

    private void setData(User user) {
        mail.setText(user.getEmail());
        phone.setText(user.getPhone());
        country.setText(getString(getResources().getIdentifier(user.getCountry(), "string", getActivity().getPackageName())));

        if (!user.isPhoneConfirmed()) {
            confirmPhoneLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<UserWrapper> onCreateLoader(int id, Bundle args) {
        return new ProfileLoader(getActivity(), api);
    }

    @Override
    public void onLoadFinished(Loader<UserWrapper> loader, UserWrapper data) {
        if (data != null) {
            user = data.getUser();
            setData(user);
        }
    }

    @Override
    public void onLoaderReset(Loader<UserWrapper> loader) {

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
        SharedPreferencesStorage.saveUser(getActivity(), user);
        getLoaderManager().getLoader(LOADER_ID).deliverResult(new UserWrapper(user));
    }

    @Override
    public void onClick(View v) {
        if (user != null) {
            placeProperFragment(ProfileEditorFragment.class.getName(), getUserBundle());
        } else Toast.makeText(getActivity(), R.string.re_enter, Toast.LENGTH_LONG).show();
    }

    private Bundle getUserBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        return bundle;
    }
}
