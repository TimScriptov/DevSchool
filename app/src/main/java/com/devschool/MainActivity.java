package com.devschool;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.devschool.adapters.ListAdapter;
import com.devschool.adapters.ListParser;
import com.devschool.data.Preferences;
import com.devschool.module.Ads;
import com.devschool.ui.BookmarksFragment;
import com.devschool.ui.Dialogs;
import com.devschool.utils.Utils;
import com.devschool.view.MainView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.devschool.data.Constants.PREMIUM;
import static com.devschool.data.Preferences.getCheckedItemId;
import static com.devschool.data.Preferences.setCheckedItemId;

public class MainActivity extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, BillingProcessor.IBillingHandler {
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private int REQUEST_CODE_SETTINGS = 0;
    private int REQUEST_CODE_IS_READ = 1;
    private BillingProcessor billing;
    private ListAdapter listAdapter;
//    private Ads ads; TODO: ENABLE ADS
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
        listAdapter = new ListParser(ListParser.Type.HTML, this).getListAdapter();
        listItems.setLayoutManager(new LinearLayoutManager(this));
        listItems.setAdapter(listAdapter);

//        ads = new Ads(); TODO: ENABLE ADS
        billing = new BillingProcessor(this, null, this);

        navigationView.setCheckedItem(getCheckedItemId());
        onNavigationItemSelected(navigationView.getCheckedItem());
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
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.html:
                listAdapter = new ListParser(ListParser.Type.HTML, this).getListAdapter();
                listItems.setAdapter(listAdapter);
                setTitle("HTML");
                setCheckedItemId(id);
                break;
            case R.id.css:
                listAdapter = new ListParser(ListParser.Type.CSS, this).getListAdapter();
                listItems.setAdapter(listAdapter);
                setTitle("CSS");
                setCheckedItemId(id);
                break;
            case R.id.js:
                listAdapter = new ListParser(ListParser.Type.JavaScript, this).getListAdapter();
                listItems.setAdapter(listAdapter);
                setTitle("JavaScript");
                setCheckedItemId(id);
                break;
            case R.id.jquery:
                listAdapter = new ListParser(ListParser.Type.jQuery, this).getListAdapter();
                listItems.setAdapter(listAdapter);
                setTitle("jQuery");
                setCheckedItemId(id);
                break;
            case R.id.php:
                listAdapter = new ListParser(ListParser.Type.PHP, this).getListAdapter();
                listItems.setAdapter(listAdapter);
                setTitle("PHP");
                setCheckedItemId(id);
                break;
            case R.id.bootstrap:
                listAdapter = new ListParser(ListParser.Type.Bootstrap, this).getListAdapter();
                listItems.setAdapter(listAdapter);
                setTitle("Bootstrap");
                setCheckedItemId(id);
                break;
            case R.id.java:
                listAdapter = new ListParser(ListParser.Type.Java, this).getListAdapter();
                listItems.setAdapter(listAdapter);
                setTitle("Java");
                setCheckedItemId(id);
                break;
            case R.id.json:
                listAdapter = new ListParser(ListParser.Type.JSON, this).getListAdapter();
                listItems.setAdapter(listAdapter);
                setTitle("JSON");
                setCheckedItemId(id);
                break;
            case R.id.xml:
                listAdapter = new ListParser(ListParser.Type.XML, this).getListAdapter();
                listItems.setAdapter(listAdapter);
                setTitle("XML");
                setCheckedItemId(id);
                break;
            case R.id.sql:
                listAdapter = new ListParser(ListParser.Type.SQL, this).getListAdapter();
                listItems.setAdapter(listAdapter);
                setTitle("SQL");
                setCheckedItemId(id);
                break;
            case R.id.nodejs:
                listAdapter = new ListParser(ListParser.Type.NodeJS, this).getListAdapter();
                listItems.setAdapter(listAdapter);
                setTitle("NodeJS");
                setCheckedItemId(id);
                break;
            case R.id.python:
                listAdapter = new ListParser(ListParser.Type.Python, this).getListAdapter();
                listItems.setAdapter(listAdapter);
                setTitle("Python");
                setCheckedItemId(id);
                break;
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
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"timscriptov@gmail.com"});
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
            startActivityForResult(new Intent(this, LessonActivity.class)
                    .putExtra("url", url)
                    .putExtra("itemsCount", listAdapter.getItemCount())
                    .putExtra("itemsSrc", listAdapter.getItemsSrc())
                    .putExtra("isPremium", billing.isPurchased(PREMIUM))
                    .putExtra("position", position), REQUEST_CODE_IS_READ);
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
//            ads.loadInterstitial(this); TODO: ENABLE ADS
            fab.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        billing.handleActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IS_READ) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", 0);
                listAdapter.notifyItemChanged(position);
            }
            if (!Preferences.isRated()) Dialogs.rate(this);
            else if (!billing.isPurchased(PREMIUM)) {
//                ads.showInsAd(); TODO: ENABLE ADS
            }
        }
    }
}
