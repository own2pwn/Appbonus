package com.appbonus.android;

import com.activeandroid.sebbia.ActiveAndroid;
import com.activeandroid.sebbia.app.Application;
import com.dynamixsoftware.ErrorAgent;
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
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(6 * 1024 * 1024))
                .build();
        ImageLoader.getInstance().init(config);
    }
}
