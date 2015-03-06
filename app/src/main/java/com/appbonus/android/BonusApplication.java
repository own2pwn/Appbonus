package com.appbonus.android;

import com.activeandroid.sebbia.ActiveAndroid;
import com.activeandroid.sebbia.app.Application;
import com.appbonus.android.storage.Storage;
import com.dynamixsoftware.ErrorAgent;
import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class BonusApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //init active-android-sebbia
        ActiveAndroid.initialize(this);
        //configure universal-image-loader
        initImageLoader();
        //init watchcat
        ErrorAgent.register(this, getResources().getInteger(R.integer.watch_cat_project_id));

        //configure Flurry
        FlurryAgent.setLogEnabled(false);
        //init Flurry
        FlurryAgent.init(this, getString(R.string.flurry_app_key));

        //init storage
        Storage.init(this);
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(6 * 1024 * 1024))
                .build();
        ImageLoader.getInstance().init(config);
    }
}
