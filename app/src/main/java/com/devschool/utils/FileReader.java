package com.devschool.utils;

import android.util.Log;

import com.devschool.App;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.SSLException;

public class FileReader {
    @NotNull
    public static String fromAssets(String path) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(App.getContext().getAssets().open(path), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
            return sb.toString();
        } catch (Exception exception) {
            return "<p style='color:red;'>Error:</p>" + Log.getStackTraceString(exception);
        }
    }

    @NotNull
    public static String fromUrl(String url) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader((new URL(url).openStream()), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
            return sb.toString();
        } catch (Exception exception) {
            if (exception instanceof SSLException) {
                try {
                    ProviderInstaller.installIfNeeded(App.getContext());
                    return fromUrl(url);
                } catch (GooglePlayServicesRepairableException e) {
                    return Log.getStackTraceString(e);
                } catch (GooglePlayServicesNotAvailableException e) {
                    return Log.getStackTraceString(e);
                }
            }
            return "<p style='color:red;'>Error:</p>" + Log.getStackTraceString(exception);
        }
    }
}
