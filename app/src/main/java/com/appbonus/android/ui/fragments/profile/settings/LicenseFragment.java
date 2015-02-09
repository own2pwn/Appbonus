package com.appbonus.android.ui.fragments.profile.settings;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appbonus.android.R;
import com.dolphin.activity.fragment.BaseFragment;

import java.io.IOException;
import java.io.InputStream;

public class LicenseFragment extends BaseFragment {
    protected TextView answer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.license_layout, null);
        answer = (TextView) view.findViewById(R.id.answer);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            AssetManager am = getActivity().getAssets();
            InputStream is = am.open("agreement.txt");
            answer.setText(convertStreamToString(is));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle(R.string.license);
        setDrawerIndicatorEnabled(false);
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
