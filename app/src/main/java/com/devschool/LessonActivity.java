package com.devschool;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.devschool.data.Bookmarks;
import com.devschool.module.Ads;
import com.devschool.module.HtmlRenderer;
import com.devschool.ui.Dialogs;
import com.devschool.utils.FileReader;
import com.devschool.utils.ItemUtils;
import com.devschool.utils.Utils;
import com.devschool.view.NestedWebView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import static com.devschool.utils.ItemUtils.isRead;

public class LessonActivity extends AppCompatActivity implements View.OnClickListener {
    private NestedWebView webView;
    private FloatingActionButton prev_item, next_item, bookmark;
    private CollapsingToolbarLayout ctl;
    private int itemPosition;
    private int itemsCount;
    private long time = System.currentTimeMillis();
    private ArrayList<String> itemsSrc;
    private Ads ads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ctl = findViewById(R.id.collapsing_toolbar);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebClient());
        webView.setWebChromeClient(new ChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        prev_item = findViewById(R.id.prev_item);
        next_item = findViewById(R.id.next_item);
        bookmark = findViewById(R.id.bookmark_lesson);
        prev_item.setOnClickListener(this);
        next_item.setOnClickListener(this);
        bookmark.setOnClickListener(this);
        itemPosition = getIntent().getIntExtra("position", 0);
        itemsCount = getIntent().getIntExtra("itemsCount", 0);
        itemsSrc = (ArrayList<String>) getIntent().getSerializableExtra("itemsSrc");

        new PageLoader(getIntent().getStringExtra("url")).execute();

        boolean isPremium = getIntent().getBooleanExtra("isPremium", false);
        if (!isPremium) {
            ads = new Ads();
            ((LinearLayout) findViewById(R.id.adLayout)).addView(ads.getBanner(this));
        }
    }

    @Override
    public void onClick(View v) {
        if (!Utils.isNetworkAvailable()) {
            Dialogs.error(this, getString(R.string.no_connection));
            return;
        }
        switch (v.getId()) {
            case R.id.prev_item:
                new PageLoader(itemsSrc.get(--itemPosition)).execute();
                break;
            case R.id.next_item:
                new PageLoader(itemsSrc.get(++itemPosition)).execute();
                break;
            case R.id.bookmark_lesson:
                bookmark.hide();
                if (Bookmarks.isBookmarked(webView.getUrl())) {
                    Bookmarks.remove(webView.getUrl());
                    Snackbar.make(webView, R.string.bookmark_removed, Snackbar.LENGTH_SHORT).show();
                    bookmark.setImageResource(R.drawable.ic_star_white);
                } else if (Bookmarks.add(webView.getUrl(), webView.getTitle())) {
                    Snackbar.make(webView, R.string.bookmarked, Snackbar.LENGTH_SHORT).show();
                    bookmark.setImageResource(R.drawable.ic_star_yellow);
                }
                bookmark.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (time + 15000 < System.currentTimeMillis()) {
            if (!isRead(webView.getUrl())) {
                Snackbar.make(webView, R.string.mark_read, Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View p1) {
                        markRead();
                    }
                }).show();
            } else super.onBackPressed();

        } else super.onBackPressed();

        time = System.currentTimeMillis();
    }

    private void markRead() {
        if (ItemUtils.markRead(webView.getUrl(), webView.getTitle())) {
            setResult(RESULT_OK, new Intent().putExtra("isRead", true).putExtra("position", itemPosition));
            finish();
        }
    }

    private class WebClient extends WebViewClient {
        @SuppressLint("RestrictedApi")
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            bookmark.setVisibility(View.GONE);
            prev_item.setVisibility(View.GONE);
            next_item.setVisibility(View.GONE);
        }

        @SuppressLint("RestrictedApi")
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (Bookmarks.isBookmarked(url)) {
                bookmark.setImageResource(R.drawable.ic_star_yellow);
            } else bookmark.setImageResource(R.drawable.ic_star_white);
            bookmark.show();
            if (itemPosition != 0) prev_item.show();
            if (itemPosition != itemsCount - 1) next_item.show();
        }
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            ctl.setTitle(title);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class PageLoader extends AsyncTask<Void, Void, Void> {
        private String link;
        private String html;

        private PageLoader(String link) {
            this.link = link;
        }

        @Override
        protected Void doInBackground(Void... strings) {
            html = HtmlRenderer.renderHtml(FileReader.fromUrl(link));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            webView.loadDataWithBaseURL(link, html, "text/html", "UTF-8", link);
        }
    }
}
