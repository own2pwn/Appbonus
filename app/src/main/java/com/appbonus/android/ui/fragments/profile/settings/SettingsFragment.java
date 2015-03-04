package com.appbonus.android.ui.fragments.profile.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.appbonus.android.R;
import com.appbonus.android.model.User;
import com.appbonus.android.model.api.UserWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.appbonus.android.ui.fragments.profile.OnUserUpdateListener;
import com.appbonus.android.ui.fragments.profile.settings.faq.FaqListFragment;
import com.dolphin.helper.IntentHelper;
import com.dolphin.ui.fragment.SimpleFragment;

public class SettingsFragment extends SimpleFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    protected CheckBox showPush;
    protected CheckBox pushSound;

    protected Button webCab;
    protected Button faq;
    protected Button license;
    protected Button exit;

    protected TextView enterAs;

    protected User user;

    protected SettingsFragmentListener listener;

    public interface SettingsFragmentListener {
        UserWrapper writeProfile(User user) throws Throwable;

        void exit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (SettingsFragmentListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("user");
        if (user == null) {
            user = SharedPreferencesStorage.getUser(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_layout, null);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.settings);
        setData();
        setDrawerIndicatorEnabled(false);
    }

    private void setData() {
        showPush.setChecked(user.isNotify());
        pushSound.setChecked(user.isNotifySound());

        enterAs.setText(user.getEmail());

        showPush.setOnCheckedChangeListener(this);
        pushSound.setOnCheckedChangeListener(this);
    }

    private void initUI(View view) {
        showPush = (CheckBox) view.findViewById(R.id.show_push);
        pushSound = (CheckBox) view.findViewById(R.id.push_sound);

        webCab = (Button) view.findViewById(R.id.web_cab);
        webCab.setOnClickListener(this);

        faq = (Button) view.findViewById(R.id.faq);
        faq.setOnClickListener(this);

        license = (Button) view.findViewById(R.id.license);
        license.setOnClickListener(this);

        exit = (Button) view.findViewById(R.id.exit);
        exit.setOnClickListener(this);

        enterAs = (TextView) view.findViewById(R.id.enter_as);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.web_cab:
                startActivity(IntentHelper.openLink(getString(R.string.my_web_cab_link)));
                break;
            case R.id.faq:
                placeProperFragment(FaqListFragment.class.getName());
                break;
            case R.id.license:
                openLicense();
                break;
            case R.id.exit:
                listener.exit();
                break;
        }
    }

    private void openLicense() {
        placeProperFragment(LicenseFragment.class.getName());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.show_push:
                user.setNotify(isChecked);
                break;
            case R.id.push_sound:
                user.setNotifySound(isChecked);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.writeProfile(user);
                } catch (Throwable ignored) {
                }
            }
        }).start();
        ((OnUserUpdateListener) getTargetFragment()).onUpdate(user);
        super.onDestroyView();
    }
}
