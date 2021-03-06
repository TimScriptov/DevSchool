package com.devschool.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import com.devschool.App;

import org.jetbrains.annotations.NotNull;

public class Utils {
    public static boolean isNetworkAvailable() {
        ConnectivityManager connection = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connection.getActiveNetworkInfo();
        if (info == null) return false;
        else return info.isConnected();
    }

    @NotNull
    public static String fromBase64(String message) {
        byte[] data = Base64.decode(message, Base64.DEFAULT);
        return new String(data);
    }

    @NotNull
    public static String reverseString(String string) {
        return new StringBuilder(string).reverse().toString();
    }

}

