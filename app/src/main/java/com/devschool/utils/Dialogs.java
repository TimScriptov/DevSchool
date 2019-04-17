package com.devschool.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import com.devschool.R;
import com.devschool.data.Preferences;

import es.dmoral.toasty.Toasty;


public class Dialogs {

    public static void error(Context context, String text) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.error)
                .setMessage(text)
                .setPositiveButton("ОК", null)
                .create()
                .show();
    }

    public static void rate(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.rate, null);
        RatingBar ratingBar = view.findViewById(R.id.rating_bar);

        final AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating > 2) {
                    Preferences.setRated();
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.devschool")));
                } else Toasty.info(context, R.string.thanks).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
