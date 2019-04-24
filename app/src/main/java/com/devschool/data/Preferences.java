package com.devschool.data;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.devschool.App;
import com.devschool.R;

public final class Preferences {

    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());

    public static String getFontSize() {
        return preferences.getString("font_size", "100%");
    }

    public static String getTheme() {
        return preferences.getString("theme", "auto");
    }

    public static boolean translatePluginEnabled() {
        return preferences.getBoolean("translate", false);
    }

    public static void setRated() {
        preferences.edit().putBoolean("isRated", true).apply();
    }

    public static boolean isRated() {
        return preferences.getBoolean("isRated", false);
    }

    public static int getCheckedItemId() {
        return preferences.getInt("checkedItemId", R.id.html);
    }

    public static void setCheckedItemId(int id) {
        preferences.edit().putInt("checkedItemId", id).apply();
    }
}
