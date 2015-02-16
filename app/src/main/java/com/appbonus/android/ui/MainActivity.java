package com.appbonus.android.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.api.model.DeviceRequest;
import com.appbonus.android.model.Notification;
import com.appbonus.android.model.api.DataWrapper;
import com.appbonus.android.push.BonusGCMUtils;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.appbonus.android.ui.fragments.balance.BalanceBrowserFragment;
import com.appbonus.android.ui.fragments.friends.FriendsFragment;
import com.appbonus.android.ui.fragments.navigation.NavigationDrawerFragment;
import com.appbonus.android.ui.fragments.offer.OfferListFragment;
import com.appbonus.android.ui.fragments.profile.ProfileBrowserFragment;
import com.appbonus.android.ui.login.LoginActivity;
import com.dolphin.asynctask.DialogExceptionalAsyncTask;
import com.dolphin.ui.SimpleActivity;
import com.dolphin.ui.fragment.NavigationDrawer;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.mobileapptracker.MobileAppTracker;

import java.io.Serializable;

public class MainActivity extends SimpleActivity implements NavigationDrawer.NavigationDrawerCallbacks {
    public static final String OFFERS_FRAGMENT = OfferListFragment.class.getName();
    public static final String PROFILE_FRAGMENT = ProfileBrowserFragment.class.getName();
    public static final String BALANCE_FRAGMENT = BalanceBrowserFragment.class.getName();
    public static final String FRIENDS_FRAGMENT = FriendsFragment.class.getName();

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Api api;
    private MobileAppTracker mobileAppTracker;

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
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                Serializable object = extras.getSerializable("notification");
                if (object instanceof Notification) {
                    openNotificationFragment(((Notification) object));
                } else openDefaultFragment();
            } else openDefaultFragment();
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout)
        );
        initMobileAppTracker();
    }

    private void openNotificationFragment(Notification notification) {
        if (Notification.BALANCE.equals(notification.getNotificationType())) {
            openBaseFragment(BALANCE_FRAGMENT);
        } else if (Notification.REFERRALS.equals(notification.getNotificationType())) {
            openBaseFragment(FRIENDS_FRAGMENT);
        } else openDefaultFragment();
    }

    private void initMobileAppTracker() {
        MobileAppTracker.init(getApplicationContext(), getString(R.string.mobile_app_tracking_advertiser_id), getString(R.string.mobile_app_tracking_conversion_key));
        mobileAppTracker = MobileAppTracker.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.Info idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                    mobileAppTracker.setGoogleAdvertisingId(idInfo.getId(), idInfo.isLimitAdTrackingEnabled());
                } catch (Exception e) {
                    mobileAppTracker.setAndroidId(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                }
            }
        }).start();
    }

    private void openDefaultFragment() {
        openBaseFragment(OFFERS_FRAGMENT);
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
                        new BonusGCMUtils().getRegistrationId(context)));
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
            case NavigationDrawerFragment.NAV_PROFILE:
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

    @Override
    public void setDrawerIndicatorEnabled(boolean enable) {
        mNavigationDrawerFragment.setDrawerIndicatorEnabled(enable);
    }

    @Override
    public void toggleDrawer() {
        mNavigationDrawerFragment.toggle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, getString(R.string.flurry_app_key));
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mobileAppTracker.setReferralSources(this);
        mobileAppTracker.measureSession();
    }
}
