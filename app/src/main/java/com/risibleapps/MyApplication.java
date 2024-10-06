package com.risibleapps;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.ads.MobileAds;

public class MyApplication extends Application {

    private static AppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, initializationStatus -> {});
        appOpenManager = new AppOpenManager(this);

        //disabling night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }

}
