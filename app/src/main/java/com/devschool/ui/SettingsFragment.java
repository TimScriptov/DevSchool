package com.devschool.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.devschool.R;
import com.devschool.module.DayNight;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private ListPreference fontSize;
    private ListPreference theme;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "theme":
                AppCompatDelegate.setDefaultNightMode(DayNight.getUserSlectedMode());
                getActivity().finish();
                break;
            case "font_size":
                fontSize.setSummary(fontSize.getValue() + "%");
        }
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings);
        fontSize = (ListPreference) findPreference("font_size");
        theme = (ListPreference) findPreference("theme");
        fontSize.setSummary(fontSize.getValue() + "%");
        theme.setSummary(theme.getEntry());
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
}
