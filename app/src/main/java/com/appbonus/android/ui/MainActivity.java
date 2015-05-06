package com.appbonus.android.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.api.model.ChangePasswordRequest;
import com.appbonus.android.api.model.ConfirmPhoneRequest;
import com.appbonus.android.api.model.DeviceRequest;
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
import com.appbonus.android.storage.Config;
import com.appbonus.android.storage.Storage;
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
import com.appbonus.android.ui.fragments.profile.settings.faq.FaqListFragment;
import com.appbonus.android.ui.login.LoginActivity;
import com.dolphin.asynctask.DialogExceptionalAsyncTask;
import com.dolphin.asynctask.ExceptionAsyncTask;
import com.dolphin.helper.IntentHelper;
import com.dolphin.net.methods.BaseHttpMethod;
import com.dolphin.push.GoogleCloudMessagingUtils;
import com.dolphin.ui.SimpleActivity;
import com.dolphin.ui.fragment.NavigationDrawer;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mobileapptracker.MobileAppTracker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    public static final String FAQ_FRAGMENT = FaqListFragment.class.getName();

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
        showGoogleServicesMessageIfShould();
        showUpdateAppMessageIfShould();
    }

    private void showUpdateAppMessageIfShould() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return;
        }
        int versionCode = info.versionCode;
        Integer serverVersionCode = Storage.load(this, Config.VERSION_CODE);
        final String appUrl = Storage.load(this, Config.APP_URL);
        if (serverVersionCode != null && serverVersionCode != 0
                && serverVersionCode > versionCode && !TextUtils.isEmpty(appUrl)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.your_app_is_too_old)
                    .setNegativeButton(R.string.menu_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(R.string.menu_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downloadNewVersionAndInstall(appUrl);
                        }
                    }).show();
        }
    }

    private void downloadNewVersionAndInstall(String appUrl) {
        try {
            String apkFileName = "update.apk";

            URL url = new URL(appUrl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String path = getFileStreamPath(Environment.DIRECTORY_DOWNLOADS).getPath();
            File file = new File(path);
            file.mkdirs();
            File outputFile = new File(file, apkFileName);
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(path + apkFileName)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } catch (IOException e) {
            Toast.makeText(this, R.string.update_error, Toast.LENGTH_LONG).show();
        }
    }

    private void showGoogleServicesMessageIfShould() {
        int googleServicesResultCode = Storage.load(this, Config.GOOGLE_SERVICES_RESULT_CODE, ConnectionResult.SUCCESS);
        boolean googleServicesMessageShown = Storage.load(this, Config.GOOGLE_SERVICES_UNAVAILABLE_MESSAGE_SHOWN, false);
        if (googleServicesResultCode != ConnectionResult.SUCCESS && !googleServicesMessageShown) {
            GooglePlayServicesUtil.getErrorDialog(googleServicesResultCode, this,
                    GoogleCloudMessagingUtils.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            Storage.save(this, Config.GOOGLE_SERVICES_UNAVAILABLE_MESSAGE_SHOWN, true);
        }
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
        closeDrawerIfOpened();
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
            case R.id.faq:
                openBaseFragment(FAQ_FRAGMENT);
                return true;
            case R.id.exit:
                exit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeDrawerIfOpened() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.toggle();
        }
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
        Storage.delete(this, Config.TOKEN);
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
            case NavigationDrawerFragment.NAV_FAQ:
                openBaseFragment(FAQ_FRAGMENT);
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
        return api.makeWithdrawal(new com.appbonus.android.api.model.WithdrawalRequest(request));
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
        return api.unregisterDevice(new DeviceRequest(new BonusGCMUtils().getRegistrationId(this)));
    }

    @Override
    public UserWrapper writeProfile(User user) throws Throwable {
        return api.writeProfile(new UserRequest(user));
    }

    @Override
    public DataWrapper requestConfirmation() throws Throwable {
        return api.requestConfirmation();
    }

    @Override
    public DataWrapper confirmPhone(String code) throws Throwable {
        return api.confirmPhone(new ConfirmPhoneRequest(code));
    }

    protected void loadSettings() {
        new ExceptionAsyncTask<Void, Void, SettingsWrapper>(this) {
            @Override
            protected SettingsWrapper background(Void... params) throws Throwable {
                return api.getSettings();
            }

            @Override
            protected void onPostExecute(SettingsWrapper settingsWrapper) {
                super.onPostExecute(settingsWrapper);
                if (settingsWrapper != null && settingsWrapper.getSettings() != null) {
                    Storage.save(context, Config.SETTINGS, settingsWrapper.getSettings());
                }
            }
        }.execute();
    }

    @Override
    public void sendInviteMessage() {
        User user = getUser();
        com.appbonus.android.model.Settings settings = Storage.load(this, Config.SETTINGS, com.appbonus.android.model.Settings.class);
        String inviteCode = user.getInviteCode();
        String link = String.format(getString(R.string.promo_link), inviteCode);

        int value = Double.valueOf(settings.getPartnerSignUpBonus()).intValue();
        String partnerSignBonus = String.valueOf(value) + " " + getString(value == 1 ? R.string.rouble_1 : value < 5 ? R.string.rouble_2 : R.string.rouble_3);
        String promoText = String.format(getString(R.string.promo_text), link, inviteCode, partnerSignBonus);
        String twitterText = String.format(getString(R.string.promo_text_twitter),
                link, inviteCode, partnerSignBonus);

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, promoText);
        Intent[] intents = IntentHelper.createSharingIntent(this, promoText, twitterText);
        startActivity(Intent.createChooser(intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents), getString(R.string.share_chooser_text)));
    }

    @Override
    public User getUser() {
        return Storage.load(this, Config.USER, User.class);
    }

    @Override
    public void onTechSupportCall() {
        startActivity(IntentHelper.sendTextEmail(null, getString(R.string.app_bonus_user_question),
                new String[]{getString(R.string.app_bonus_mail)}));
    }
}
