package com.devschool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.devschool.adapters.ListAdapter;
import com.devschool.module.Ads;
import com.devschool.module.ListParser;
import com.devschool.ui.BookmarksFragment;
import com.devschool.utils.Dialogs;
import com.devschool.utils.Utils;
import com.devschool.view.MainView;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.devschool.data.Constants.PREMIUM;

public class MainActivity extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, BillingProcessor.IBillingHandler {
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private int REQUEST_CODE_SETTINGS = 0;
    private BillingProcessor billing;
    private ListAdapter listAdapter;
    private Ads ads;
    private RecyclerView listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        listItems = findViewById(R.id.items);
        fab = findViewById(R.id.premium);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billing.purchase(MainActivity.this, PREMIUM, PREMIUM);
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listItems.setLayoutManager(new LinearLayoutManager(this));
        listItems.setAdapter(new ListParser(ListParser.Type.HTML, this).getListAdapter());

        ads = new Ads();
        billing = new BillingProcessor(this, null, this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_bookmarks:
                new BookmarksFragment().show(getSupportFragmentManager(), null);
                break;
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.nav_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), REQUEST_CODE_SETTINGS);
                break;
            case R.id.nav_exit:
                finish();
                break;
            case R.id.nav_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download DevSchool from https://play.google.com/store/apps/details?id=com.devschool");
                startActivity(shareIntent);
                break;
            case R.id.nav_send:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("email"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"elmurzaev.ram@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "DevSchool");
                intent.setType("message/rfc822");
                Intent chooser = Intent.createChooser(intent, "Email");
                startActivity(chooser);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public void openLesson(String url, int position) {
        if (Utils.isNetworkAvailable()) {

        } else Dialogs.error(this, getString(R.string.no_connection));
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        listAdapter.getFilter().filter(s);
        return false;
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toasty.success(this, getString(R.string.premium_activated)).show();
        fab.hide();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        if (errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
            Toasty.error(this, getString(R.string.purchase_canceled)).show();
        }
    }

    @Override
    public void onBillingInitialized() {
        if (!billing.isPurchased(PREMIUM)) {
            ads.loadInterstitial(this);
            fab.show();
        }
    }
}
