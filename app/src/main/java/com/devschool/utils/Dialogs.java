package com.devschool.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.devschool.R;


public class Dialogs {
    public static void error(Context context, String text) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.error)
                .setMessage(text)
                .setPositiveButton("ОК", null)
                .create()
                .show();
    }
}
