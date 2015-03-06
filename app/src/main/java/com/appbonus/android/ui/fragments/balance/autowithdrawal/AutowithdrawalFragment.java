package com.appbonus.android.ui.fragments.balance.autowithdrawal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.appbonus.android.R;
import com.appbonus.android.component.FloatLabel;
import com.appbonus.android.storage.Config;
import com.appbonus.android.storage.Storage;
import com.dolphin.ui.fragment.SimpleFragment;

public class AutowithdrawalFragment extends SimpleFragment implements CompoundButton.OnCheckedChangeListener {
    protected CheckBox auto;
    protected FloatLabel qiwi;
    protected FloatLabel mobile;

    protected AutowithdrawalFragmentListener listener;

    public interface AutowithdrawalFragmentListener {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (AutowithdrawalFragmentListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.autowithdrawal_layout, null);
        initUI(view);
        setData(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.auto_withdrawal);
        setDrawerIndicatorEnabled(false);
    }

    private void initUI(View view) {
        auto = (CheckBox) view.findViewById(R.id.auto_withdrawal);
        qiwi = (FloatLabel) view.findViewById(R.id.qiwi);
        mobile = (FloatLabel) view.findViewById(R.id.mobile);

        auto.setOnCheckedChangeListener(this);
    }

    private void setData(Context context) {
        auto.setChecked(Storage.<Boolean>load(context, Config.AUTO_WITHDRAWAL));
        qiwi.setText(Storage.<String>load(context, Config.QIWI));
        mobile.setText(Storage.<String>load(context, Config.MOBILE));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Storage.save(getActivity(), Config.AUTO_WITHDRAWAL, isChecked);

        if (isChecked) {
            qiwi.unlock();
            mobile.unlock();
        } else {
            qiwi.lock();
            mobile.lock();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!qiwi.isLocked()) {
            Storage.save(getActivity(), Config.QIWI, qiwi.getText());
        }
        if (!mobile.isLocked()) {
            Storage.save(getActivity(), Config.MOBILE, mobile.getText());
        }
    }
}
