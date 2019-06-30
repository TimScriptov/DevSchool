package com.devschool.module;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import org.jetbrains.annotations.NotNull;

import static com.devschool.data.Preferences.getTheme;

public class DayNight {
    public static Mode getCurrentMode(@NotNull Context context) {
        int currentNightMode = context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                return Mode.DAY;
            case Configuration.UI_MODE_NIGHT_YES:
                return Mode.NIGHT;
            default:
                return Mode.AUTO;
        }
    }

    public static int getUserSlectedMode() {
        switch (getTheme()) {
            case "day":
                return AppCompatDelegate.MODE_NIGHT_NO;
            case "night":
                return AppCompatDelegate.MODE_NIGHT_YES;
            default:
                return AppCompatDelegate.MODE_NIGHT_AUTO;
        }
    }

    public enum Mode {
        DAY, NIGHT, AUTO
    }
}
