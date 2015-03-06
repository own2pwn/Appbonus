package com.appbonus.android.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.api.model.ChangePasswordRequest;
import com.appbonus.android.api.model.ConfirmPhoneRequest;
import com.appbonus.android.api.model.DeviceRequest;
import com.appbonus.android.api.model.SimpleRequest;
import com.appbonus.android.api.model.UserRequest;
import com.appbonus.android.loaders.BalanceLoader;
import com.appbonus.android.loaders.HistoryLoader;
import com.appbonus.android.loaders.OfferLoader;
import com.appbonus.android.loaders.OffersLoader;
import com.appbonus.android.loaders.ProfileLoader;
import com.appbonus.android.loaders.ReferralsDetailsLoader;
import com.appbonus.android.loaders.ReferralsHistoryLoader;
import com.appbonus.android.model.Notification;
import com.appbonus.android.model.Offer;
import com.appbonus.android.model.User;
import com.appbonus.android.model.WithdrawalRequest;
import com.appbonus.android.model.api.BalanceWrapper;
import com.appbonus.android.model.api.DataWrapper;
import com.appbonus.android.model.api.HistoryWrapper;
import com.appbonus.android.model.api.OfferWrapper;
import com.appbonus.android.model.api.OffersWrapper;
import com.appbonus.android.model.api.ReferralsDetailsWrapper;
import com.appbonus.android.model.api.ReferralsHistoryWrapper;
import com.appbonus.android.model.api.SettingsWrapper;
import com.appbonus.android.model.api.UserWrapper;
import com.appbonus.android.push.BonusGCMUtils;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.appbonus.android.ui.fragments.balance.BalanceBrowserFragment;
import com.appbonus.android.ui.fragments.balance.withdrawal.WithdrawalFragment;
import com.appbonus.android.ui.fragments.common.OnTechSupportCallListener;
import com.appbonus.android.ui.fragments.friends.FriendsFragment;
import com.appbonus.android.ui.fragments.friends.MeetFriendsFragment;
import com.appbonus.android.ui.fragments.navigation.NavigationDrawerFragment;
import com.appbonus.android.ui.fragments.offer.OfferBrowserFragment;
import com.appbonus.android.ui.fragments.offer.OfferListFragment;
import com.appbonus.android.ui.fragments.profile.ConfirmPhoneFragment;
import com.appbonus.android.ui.fragments.profile.ProfileBrowserFragment;
import com.appbonus.android.ui.fragments.profile.ProfileEditorFragment;
import com.appbonus.android.ui.fragments.profile.settings.SettingsFragment;
import com.appbonus.android.ui.login.LoginActivity;
import com.dolphin.asynctask.DialogExceptionalAsyncTask;
import com.dolphin.asynctask.ExceptionAsyncTask;
import com.dolphin.helper.IntentHelper;
import com.dolphin.net.methods.BaseHttpMethod;
import com.dolphin.ui.SimpleActivity;
import com.dolphin.ui.fragment.NavigationDrawer;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.mobileapptracker.MobileAppTracker;

public class MainActivity extends SimpleActivity implements NavigationDrawer.NavigationDrawerCallbacks,
        OfferListFragment.OffersListFragmentListener, OfferBrowserFragment.OfferBrowserFragmentListener,
        FriendsFragment.FriendsFragmentListener, BalanceBrowserFragment.BalanceBrowserFragmentListener,
        WithdrawalFragment.WithdrawalFragmentListener,
        ProfileBrowserFragment.ProfileBrowserFragmentListener, ProfileEditorFragment.ProfileEditorFragmentListener,
        ConfirmPhoneFragment.ConfirmPhoneFragmentListener, SettingsFragment.SettingsFragmentListener,
        MeetFriendsFragment.MeetFriendsFragmentListener,
        OnTechSupportCallListener {
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
                Parcelable object = extras.getParcelable("notification");
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
        initLoadingDialog(getString(R.string.loading));
        loadSettings();
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

    @Override
    public void exit() {
        new DialogExceptionalAsyncTask<Void, Void, DataWrapper>(this) {
            @Override
            protected FragmentManager getFragmentManager() {
                return getSupportFragmentManager();
            }

            @Override
            protected DataWrapper background(Void... params) throws Throwable {
                return unregisterDevice();
            }

            @Override
            protected void onPostExecute(DataWrapper dataWrapper) {
                super.onPostExecute(dataWrapper);
                cleanStorage();
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            }
        }.execute();
    }

    private void cleanStorage() {
        SharedPreferencesStorage.deleteToken(this);
        BaseHttpMethod.resetCaches();
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

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.toggle();
        } else
            super.onBackPressed();
    }

    @Override
    public Loader<OffersWrapper> createOffersLoader(long page) {
        return new OffersLoader(this, api, page);
    }

    @Override
    public Loader<OfferWrapper> createOfferLoader(Offer offer) {
        return new OfferLoader(this, api, offer);
    }

    @Override
    public Loader<ReferralsDetailsWrapper> createReferralsDetailsLoader() {
        return new ReferralsDetailsLoader(this, api);
    }

    @Override
    public Loader<ReferralsHistoryWrapper> createReferralsHistoryLoader(long page) {
        return new ReferralsHistoryLoader(this, api, page);
    }

    @Override
    public Loader<BalanceWrapper> createBalanceLoader() {
        return new BalanceLoader(this, api);
    }

    @Override
    public Loader<HistoryWrapper> createHistoryLoader(long page) {
        return new HistoryLoader(this, api, page);
    }

    @Override
    public DataWrapper makeWithdrawal(WithdrawalRequest request) throws Throwable {
        return api.makeWithdrawal(new com.appbonus.android.api.model.WithdrawalRequest(
                getToken(), request));
    }

    @Override
    public Loader<UserWrapper> createUserLoader() {
        return new ProfileLoader(this, api);
    }

    @Override
    public UserWrapper changePassword(ChangePasswordRequest request) throws Throwable {
        return api.changePassword(request);
    }

    public DataWrapper unregisterDevice() throws Throwable {
        return api.unregisterDevice(new DeviceRequest(getToken(),
                new BonusGCMUtils().getRegistrationId(this)));
    }

    @Override
    public UserWrapper writeProfile(User user) throws Throwable {
        return api.writeProfile(new UserRequest(getToken(), user));
    }

    @Override
    public DataWrapper requestConfirmation() throws Throwable {
        return api.requestConfirmation(new SimpleRequest(getToken()));
    }

    @Override
    public DataWrapper confirmPhone(String code) throws Throwable {
        return api.confirmPhone(new ConfirmPhoneRequest(getToken(), code));
    }

    protected void loadSettings() {
        new ExceptionAsyncTask<Void, Void, SettingsWrapper>(this) {
            @Override
            protected SettingsWrapper background(Void... params) throws Throwable {
                return api.getSettings(new SimpleRequest(getToken()));
            }

            @Override
            protected void onPostExecute(SettingsWrapper settingsWrapper) {
                super.onPostExecute(settingsWrapper);
                if (settingsWrapper != null && settingsWrapper.getSettings() != null) {
                    SharedPreferencesStorage.saveSettings(context, settingsWrapper.getSettings());
                }
            }
        }.execute();
    }

    protected String getToken() {
        return SharedPreferencesStorage.getToken(this);
    }

    @Override
    public void sendInviteMessage() {
        User user = getUser();
        com.appbonus.android.model.Settings settings = SharedPreferencesStorage.getSettings(this);
        String promoText = String.format(getString(R.string.promo_text),
                "link", user.getInviteCode(), String.valueOf(Double.valueOf(settings.getPartnerSignUpBonus()).intValue()));
        String twitterText = String.format(getString(R.string.promo_text_twitter),
                "link", user.getInviteCode(), String.valueOf(Double.valueOf(settings.getPartnerSignUpBonus()).intValue()));


        startActivity(Intent.createChooser(IntentHelper.createSharingIntent(this, promoText, twitterText), getString(R.string.share_chooser_text)));
    }

    @Override
    public User getUser() {
        return SharedPreferencesStorage.getUser(this);
    }

    @Override
    public void onTechSupportCall() {
        startActivity(IntentHelper.sendTextEmail(null, getString(R.string.app_bonus_user_question),
                new String[]{getString(R.string.app_bonus_mail)}));
    }
}
