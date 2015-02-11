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
import com.dolphin.utils.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public abstract class SimpleActivity extends ActionBarActivity {
    protected Toolbar toolbar;

    @SuppressWarnings("unused")
    private static final String TAG = Log.getNormalizedTag(SimpleActivity.class);
    protected List<WeakReference<Fragment>> fragList = new CopyOnWriteArrayList<>();

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

    public void placeProperFragment(String fragmentTag, boolean addToBackStackCustom) {

        placeProperFragment(fragmentTag, null, addToBackStackCustom, null);
    }

    public void placeProperFragment(String fragmentTag) {

        placeProperFragment(fragmentTag, true);
    }

    protected List<String> getExcluded() {
        return new ArrayList<>();
    }

    public void placeProperFragment(String fragmentTag, Bundle args, boolean addToBackStackCustom, Fragment targetFragment) {
        if (targetFragment != null && fragmentTag.equals(targetFragment.getTag())) return;

        Log.d(TAG, "placeProperFragment " + fragmentTag);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        boolean addToBackStack = !isFragmentContainerEmpty(fragmentManager) && addToBackStackCustom;

        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment == null || getExcluded().contains(fragmentTag)) {
            fragment = Fragment.instantiate(this, fragmentTag);
            fragment.setTargetFragment(targetFragment, -1);
        }
        if (args != null && fragment instanceof SimpleFragment) ((SimpleFragment) fragment).putArguments(args);
        if (fragment instanceof SimpleFragment) {
            ((SimpleFragment) fragment).setToolbar(getToolbar());
        }

        transaction.replace(getContainerLayout(), fragment, fragmentTag);

        transaction.addToBackStack(addToBackStack ? fragmentTag : null);

        transaction.commit();
    }

    public void closeCurrentFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.remove(fragment);
            fragmentManager.popBackStack();

            transaction.commit();
        } catch (Throwable th) {
            Log.e("closeCurrentFragment", th);
        }
    }

    protected void closeAll() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        List<Fragment> fragments = getActiveFragments();

        for (Fragment fragment : fragments) {
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
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        fragList.add(new WeakReference<>(fragment));
    }

    public void onDestroyFragment(Fragment fragment) {
        WeakReference reference = null;
        for (WeakReference<Fragment> weakReference : fragList) {
            String tag = fragment.getTag();
            if (tag != null && weakReference.get() != null && tag.equals(weakReference.get().getTag())) {
                reference = weakReference;
                break;
            }
        }
        if (reference != null) {
            fragList.remove(reference);
        }
    }

    public List<Fragment> getActiveFragments() {
        ArrayList<Fragment> ret = new ArrayList<>();
        for (WeakReference<Fragment> ref : fragList) {
            Fragment f = ref.get();
            if (f != null) {
                if (f.isVisible()) {
                    ret.add(f);
                }
            }
        }
        return ret;
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
