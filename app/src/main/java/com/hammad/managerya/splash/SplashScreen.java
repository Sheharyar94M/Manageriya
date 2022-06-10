package com.hammad.managerya.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.hammad.managerya.BuildConfig;
import com.hammad.managerya.R;
import com.hammad.managerya.mainActivity.HomeScreenActivity;

public class SplashScreen extends AppCompatActivity {

    ImageView imageViewSplash;
    TextView textViewSlogan,textViewDescription;

    static int SPLASH_SCREEN = 3500;
    Animation topAnim, bottomAnim;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //initializing interstitial ad
        MobileAds.initialize(this, initializationStatus -> {});

        //initializing views
        imageViewSplash = findViewById(R.id.img_splash);
        textViewSlogan = findViewById(R.id.text_slogan);
        textViewDescription = findViewById(R.id.text_description);

        //initializing animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //setting animations
        imageViewSplash.setAnimation(topAnim);
        textViewSlogan.setAnimation(bottomAnim);
        textViewDescription.setAnimation(bottomAnim);

        //loading the ad
        loadAd();

        //delaying the splash screen for 3.5 seconds
        new Handler().postDelayed(this::showAd, SPLASH_SCREEN);
    }

    public void loadAd() {

        //checking whether app is running on release/debug version
        String interstitialAdId="";
        if(BuildConfig.DEBUG)
        {
            interstitialAdId=getString(R.string.interstitial_ad_debug_id);
            Log.i("INTER_AD_ID", "debug version: "+interstitialAdId);
        }
        else {
            //interstitialAdId=getString(R.string.interstitial_ad_id);
            Log.i("INTER_AD_ID", "release version: "+interstitialAdId);
        }

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, interstitialAdId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.i("FAILED_AD", "splash interstitial failed ad: "+loadAdError.getCode());
                mInterstitialAd = null;
            }
        });
    }

    public void showAd() {
        //checking if ad is loaded or not
        if (mInterstitialAd != null) {
            mInterstitialAd.show(SplashScreen.this);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    mInterstitialAd = null;

                    //moving to Home Screen
                    startActivity(new Intent(SplashScreen.this, HomeScreenActivity.class));
                    finish();
                }

                //this function make sure no ad is loaded for second time
                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();

                    mInterstitialAd = null;
                }
            });
        }
        else
        {
            //if there is no internet connection, then no ad will be loaded and app will move onto Home Screen
            Intent intent = new Intent(SplashScreen.this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mInterstitialAd = null;
    }

    @Override
    protected void onDestroy() {

        mInterstitialAd = null;
        super.onDestroy();
    }
}