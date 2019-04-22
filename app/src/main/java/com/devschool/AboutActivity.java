package com.devschool;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.devschool.utils.Dialogs;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            ((TextView) findViewById(R.id.version)).append(" " + getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA).versionName);
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        findViewById(R.id.rateApp).setOnClickListener(this);
        findViewById(R.id.moreApps).setOnClickListener(this);
        findViewById(R.id.privacy).setOnClickListener(this);
    }

    @Override
    public void onClick(View p1) {
        switch (p1.getId()) {
            case R.id.rateApp:
                Dialogs.rate(this);
                break;
            case R.id.moreApps:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Ramzan Elmurzaev")));
                break;
            case R.id.privacy:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://elmurzaev.github.io/privacy/privacy_policy_ds.html")));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
