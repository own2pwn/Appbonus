package com.appbonus.android;

import com.activeandroid.sebbia.ActiveAndroid;
import com.activeandroid.sebbia.app.Application;
import com.dynamixsoftware.ErrorAgent;
import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.net.CookieManager;
import java.net.CookiePolicy;

public class BonusApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        CookieManager.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        initImageLoader();
        ErrorAgent.register(this, getResources().getInteger(R.integer.watch_cat_project_id));
        // configure Flurry
        FlurryAgent.setLogEnabled(false);

        // init Flurry
        FlurryAgent.init(this, getString(R.string.flurry_app_key));
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(6 * 1024 * 1024))
                .build();
        ImageLoader.getInstance().init(config);
    }
}
