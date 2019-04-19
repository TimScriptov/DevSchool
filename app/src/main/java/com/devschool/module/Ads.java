package com.devschool.module;

import android.content.Context;

import com.devschool.BuildConfig;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class Ads {
    private final String ADMOB_BANNER_ID = BuildConfig.DEBUG ? "ca-app-pub-3940256099942544/6300978111" : "ca-app-pub-9961237670339903/5503865823";
    private final String ADMOB_INTERSTITIAL_ID = BuildConfig.DEBUG ? "ca-app-pub-3940256099942544/1033173712" : "ca-app-pub-9961237670339903/6915971686";
    private long lastShownTime = 0;
    private AdView adView;
    private InterstitialAd interstitialAd;

    public static void initialize(Context c) {
        MobileAds.initialize(c, "ca-app-pub-9961237670339903~5243797362");
    }

    public AdView getBanner(Context context) {
        adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(ADMOB_BANNER_ID);
        adView.loadAd(new AdRequest.Builder().build());
        return adView;
    }

    public void loadInterstitial(Context context) {
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
        interstitialAd.setAdUnitId(ADMOB_INTERSTITIAL_ID);
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void showInsAd() {
        if (interstitialAd == null) return;

        if (interstitialAd.isLoaded() && lastShownTime + 20000 < System.currentTimeMillis()) {
            interstitialAd.show();
            lastShownTime = System.currentTimeMillis();
        }
    }
}
