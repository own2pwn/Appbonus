package com.dolphin.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.dolphin.R;
import com.dolphin.ui.fragment.NavigationDrawer;
import com.dolphin.ui.fragment.SimpleFragment;
import com.dolphin.ui.fragment.root.RootFragment;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;


public abstract class SimpleActivity extends ActionBarActivity {
    protected Toolbar toolbar;

    protected Set<Fragment> heap = Collections.newSetFromMap(new WeakHashMap<Fragment, Boolean>());
    protected Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setContentView(layout());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    protected void beforeSetContentView() {
    }

    protected abstract int layout();

    public void showToast(String msg) {
        if (!this.isFinishing())
            Toast.makeText(
                    this,
                    msg,
                    Toast.LENGTH_LONG
            ).show();
    }

    public void showToast(int msg) {
        if (!this.isFinishing())
            Toast.makeText(
                    this,
                    msg,
                    Toast.LENGTH_LONG
            ).show();
    }

    public void showError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(R.string.exception);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close;
            }
        });
        builder.create().show();
    }

    public void showError(int message) {
        showError(getString(message));
    }

    public Fragment placeProperFragment(String fragmentTag, boolean forceCreate) {
        return placeProperFragment(fragmentTag, null, true, null, forceCreate);
    }

    public Fragment placeProperFragment(String fragmentTag) {
        return placeProperFragment(fragmentTag, null, true, null, false);
    }

    public Fragment placeProperFragment(String fragmentTag, Bundle args) {
        return placeProperFragment(fragmentTag, args, true, null, false);
    }

    public Fragment placeProperFragment(String fragmentTag, Bundle args, boolean addToBackStackCustom, Fragment targetFragment, boolean forceCreate) {
        if (targetFragment != null && fragmentTag.equals(targetFragment.getTag())) return null;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        boolean addToBackStack = !isFragmentContainerEmpty(fragmentManager) && addToBackStackCustom;

        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (forceCreate || fragment == null) {
            fragment = Fragment.instantiate(this, fragmentTag);
            fragment.setTargetFragment(targetFragment, -1);
        }
        if (args != null)
            fragment.setArguments(args);
        else fragment.setArguments(new Bundle());
        if (fragment instanceof SimpleFragment) {
            ((SimpleFragment) fragment).setToolbar(getToolbar());
        }

        transaction.replace(getContainerLayout(), fragment, fragmentTag);

        transaction.addToBackStack(addToBackStack ? fragmentTag : null);

        transaction.commit();
        return fragment;
    }

    public void closeCurrentFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.remove(fragment);
            fragmentManager.popBackStack();

            transaction.commit();
        } catch (Throwable ignored) {
        }
    }

    protected void openBaseFragment(String name) {
        if (!name.equals(mCurrentFragment.getTag())) {
            closeAll();
            placeProperFragment(name, true);
        }
    }

    protected void closeAll() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment fragment : getActiveFragments()) {
            if (fragment instanceof NavigationDrawer) continue;

            if (fragment instanceof RootFragment) {
                ((RootFragment) fragment).notMortalClose();
            }
            transaction.remove(fragment);
            fragmentManager.popBackStack();
        }
        transaction.commit();
    }

    private boolean isFragmentContainerEmpty(FragmentManager manager) {
        Fragment prevFragment = manager.findFragmentById(getContainerLayout());
        return prevFragment == null || prevFragment.isDetached();
    }

    protected abstract int getContainerLayout();

    @Override
    public void onAttachFragment(Fragment fragment) {
        heap.add(fragment);
    }

    public void onFragmentView(Fragment fragment) {
        mCurrentFragment = fragment;
    }

    public void onDestroyFragment(Fragment fragment) {
        heap.remove(fragment);
    }

    public Collection<Fragment> getActiveFragments() {
        return heap;
    }

    public void setDrawerIndicatorEnabled(boolean enable) {
    }

    public void lockNavigationDrawer() {
    }

    public void unlockNavigationDrawer() {
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    public void toggleDrawer() {
    }
}
