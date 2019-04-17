package com.devschool;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.devschool.module.Ads;
import com.devschool.module.DayNight;

import org.jetbrains.annotations.Contract;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Contract(pure = true)
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Lato.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        AppCompatDelegate.setDefaultNightMode(DayNight.getUserSlectedMode());
        Ads.initialize(this);
    }
}
