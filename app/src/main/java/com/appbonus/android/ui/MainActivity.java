package com.appbonus.android.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.api.model.DeviceRequest;
import com.appbonus.android.component.DialogExceptionalAsyncTask;
import com.appbonus.android.model.api.DataWrapper;
import com.appbonus.android.push.GoogleCloudMessagingUtils;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.appbonus.android.ui.fragments.balance.BalanceBrowserFragment;
import com.appbonus.android.ui.fragments.friends.FriendsFragment;
import com.appbonus.android.ui.fragments.navigation.NavigationDrawerFragment;
import com.appbonus.android.ui.fragments.offer.OfferListFragment;
import com.appbonus.android.ui.fragments.profile.ProfileBrowserFragment;
import com.appbonus.android.ui.login.LoginActivity;
import com.dolphin.ui.BaseActivity;
import com.dolphin.ui.fragment.NavigationDrawer;

public class MainActivity extends BaseActivity implements NavigationDrawer.NavigationDrawerCallbacks {
    public static final String OFFERS_FRAGMENT = OfferListFragment.class.getName();
    public static final String PROFILE_FRAGMENT = ProfileBrowserFragment.class.getName();
    public static final String BALANCE_FRAGMENT = BalanceBrowserFragment.class.getName();
    public static final String FRIENDS_FRAGMENT = FriendsFragment.class.getName();

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Api api;

    @Override
    protected int layout() {
        return R.layout.activity_home;
    }

    @Override
    protected int getContainerLayout() {
        return R.id.fragments_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new ApiImpl(this);
        if (savedInstanceState == null) {
            openDefaultFragment();
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout)
        );
    }

    private void openDefaultFragment() {
        placeProperFragment(OFFERS_FRAGMENT);
    }

    @Override
    protected void beforeSetContentView() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(android.R.color.white));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.offers:
                openBaseFragment(OFFERS_FRAGMENT);
                return true;
            case R.id.profile:
                openBaseFragment(PROFILE_FRAGMENT);
                return true;
            case R.id.balance:
                openBaseFragment(BALANCE_FRAGMENT);
                return true;
            case R.id.friends:
                openBaseFragment(FRIENDS_FRAGMENT);
                return true;
            case R.id.exit:

                exit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exit() {
        new DialogExceptionalAsyncTask<Void, Void, DataWrapper>(this) {
            @Override
            protected FragmentManager getFragmentManager() {
                return getSupportFragmentManager();
            }

            @Override
            protected DataWrapper background(Void... params) throws Throwable {
                return api.unregisterDevice(new DeviceRequest(SharedPreferencesStorage.getToken(context),
                        GoogleCloudMessagingUtils.getRegistrationId(context)));
            }

            @Override
            protected void onPostExecute(DataWrapper dataWrapper) {
                super.onPostExecute(dataWrapper);
                SharedPreferencesStorage.deleteToken(context);
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            }
        }.execute();
    }

    @Override
    public void onNavigationItemSelected(int position) {
        switch (position) {
            case NavigationDrawerFragment.NAV_PROFILE :
                openBaseFragment(PROFILE_FRAGMENT);
                break;
            case NavigationDrawerFragment.NAV_OFFERS:
                openBaseFragment(OFFERS_FRAGMENT);
                break;
            case NavigationDrawerFragment.NAV_BALANCE:
                openBaseFragment(BALANCE_FRAGMENT);
                break;
            case NavigationDrawerFragment.NAV_FRIENDS:
                openBaseFragment(FRIENDS_FRAGMENT);
                break;
        }
    }

    private void openBaseFragment(String name) {
        if (!name.equals(fragList.get(fragList.size() - 1).get().getTag())) {
            closeAll();
            placeProperFragment(name);
        }
    }

    @Override
    public void setDrawerIndicatorEnabled(boolean enable) {
        mNavigationDrawerFragment.setDrawerIndicatorEnabled(enable);
    }

    @Override
    public void toggleDrawer() {
        mNavigationDrawerFragment.toggle();
    }
}
