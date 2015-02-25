package com.dolphin.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.dolphin.ui.SimpleActivity;

import org.apache.commons.lang3.BooleanUtils;

import java.util.HashMap;
import java.util.Map;


public abstract class SimpleFragment extends Fragment implements StandardFragment {
    private Map<String, AlertDialog.Builder> alertDialogs = new HashMap<>();
    private Map<String, Boolean> alertDialogsVisibility = new HashMap<>();

    protected Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        for (Map.Entry<String, AlertDialog.Builder> entry : alertDialogs.entrySet()) {
            if (BooleanUtils.isTrue(alertDialogsVisibility.get(entry.getKey()))) {
                AlertDialog.Builder builder = entry.getValue();
                if (builder != null) {
                    builder.create().show();
                }
            }
        }
    }

    protected SimpleActivity getSimpleActivity() {
        return (SimpleActivity) getActivity();
    }

    protected ActionBar getActionBar() {
        SimpleActivity simpleActivity = getSimpleActivity();
        return simpleActivity != null ? simpleActivity.getSupportActionBar() : null;
    }

    protected void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
        }
    }

    protected void setHomeButtonEnabled(boolean enabled) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(enabled);
        }
    }

    protected void setTitle(int titleRes) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setTitle(titleRes);
    }

    protected void setTitle(String titleRes) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setTitle(titleRes);
    }

    protected int getScreenOrientation() {
        Activity activity = getActivity();
        if (activity != null) {
            Resources resources = activity.getResources();
            if (resources != null) {
                Configuration configuration = resources.getConfiguration();
                if (configuration != null) {
                    return configuration.orientation;
                }
            }
        }
        return Configuration.ORIENTATION_PORTRAIT;
    }

    protected void showToast(String msg) {
        SimpleActivity simpleActivity = getSimpleActivity();
        if (simpleActivity != null)
            simpleActivity.showToast(msg);
    }

    protected void showToast(int msg) {
        SimpleActivity simpleActivity = getSimpleActivity();
        if (simpleActivity != null)
            simpleActivity.showToast(msg);
    }

    protected void showError(String message) {
        SimpleActivity simpleActivity = getSimpleActivity();
        if (simpleActivity != null)
            simpleActivity.showError(message);
    }

    protected void showError(int message) {
        SimpleActivity simpleActivity = getSimpleActivity();
        if (simpleActivity != null)
            simpleActivity.showError(message);
    }

    public ComponentName getComponentName() {
        return getActivity().getComponentName();
    }

    public Bundle takeArguments() {
        return getArguments();
    }

    public boolean closeCurrentFragment() {
        SimpleActivity simpleActivity = getSimpleActivity();
        if (simpleActivity != null) {
            simpleActivity.closeCurrentFragment(this);
            return true;
        }
        return false;
    }

    public void placeProperFragment(String fragmentTag, Bundle args, boolean addToBackStackCustom, Fragment targetFragment) {
        SimpleActivity simpleActivity = getSimpleActivity();
        if (simpleActivity != null) {
            simpleActivity.placeProperFragment(fragmentTag, args, addToBackStackCustom, targetFragment, false);
        }
    }

    public void placeProperFragment(String fragmentTag) {
        placeProperFragment(fragmentTag, null, true, this);
    }

    @Override
    public void placeProperFragment(String fragmentTag, Bundle args) {
        placeProperFragment(fragmentTag, args, true, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                return closeCurrentFragment();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected AlertDialog.Builder createAlertDialogBuilder(final String tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        alertDialogs.put(tag, builder);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                alertDialogsVisibility.put(tag, Boolean.FALSE);
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                alertDialogsVisibility.put(tag, Boolean.FALSE);
            }
        });
        builder.setCancelable(true);
        return builder;
    }

    protected void showAlertDialog(String tag) {
        if (alertDialogs.containsKey(tag)) {
            AlertDialog.Builder builder = alertDialogs.get(tag);
            alertDialogsVisibility.put(tag, Boolean.TRUE);
            builder.create().show();
        }
    }

    protected void hideActionBar() {
        if (getActionBar() != null) {
            getActionBar().hide();
        }
    }

    protected void showActionBar() {
        if (getActionBar() != null) {
            getActionBar().show();
        }
    }

    public void setDrawerIndicatorEnabled(boolean enable) {
        SimpleActivity simpleActivity = getSimpleActivity();
        if (simpleActivity != null) {
            simpleActivity.setDrawerIndicatorEnabled(enable);
        }
    }

    public void lockNavigationDrawer() {
        SimpleActivity simpleActivity = getSimpleActivity();
        if (simpleActivity != null) {
            simpleActivity.lockNavigationDrawer();
        }
    }

    public void unlockNavigationDrawer() {
        SimpleActivity simpleActivity = getSimpleActivity();
        if (simpleActivity != null) {
            simpleActivity.unlockNavigationDrawer();
        }
    }

    protected View inflateView(int layout) {
        return LayoutInflater.from(getActivity()).inflate(layout, null);
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FragmentActivity activity = getActivity();
        if (activity instanceof SimpleActivity) {
            ((SimpleActivity) activity).onDestroyFragment(this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDisplayHomeAsUpEnabled(true);
        ((SimpleActivity) getActivity()).onFragmentView(this);
    }

    public void toggleDrawer() {
        SimpleActivity simpleActivity = getSimpleActivity();
        if (simpleActivity != null) {
            simpleActivity.toggleDrawer();
        }
    }
}
