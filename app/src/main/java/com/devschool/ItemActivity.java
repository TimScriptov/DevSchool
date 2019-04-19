package com.devschool;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.devschool.data.Bookmarks;
import com.devschool.utils.Dialogs;
import com.devschool.utils.FileReader;
import com.devschool.utils.HtmlRenderer;
import com.devschool.utils.ItemUtils;
import com.devschool.utils.NestedWebView;
import com.devschool.utils.Utils;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import static com.devschool.utils.ItemUtils.isRead;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {
    private NestedWebView webView;
    private FloatingActionButton prev_item, next_item, bookmark;
    private CollapsingToolbarLayout ctl;
    private int itemPosition;
    private int itemsCount;
    private long time = System.currentTimeMillis();
    private ArrayList<String> itemsSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ctl = findViewById(R.id.collapsing_toolbar);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebClient());
        webView.setWebChromeClient(new ChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
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
                    Snackbar.make(webView, R.string.bookmark_removed, BaseTransientBottomBar.LENGTH_SHORT).show();
                    bookmark.setImageResource(R.drawable.ic_star_white);
                } else if (Bookmarks.add(webView.getUrl(), webView.getTitle())) {
                    Snackbar.make(webView, R.string.bookmarked, BaseTransientBottomBar.LENGTH_SHORT).show();
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
//            prev_item.setVisibility(itemPosition != 0 ? View.VISIBLE : View.GONE);
//            next_item.setVisibility(itemPosition != itemsCount - 1 ? View.VISIBLE : View.GONE);
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
