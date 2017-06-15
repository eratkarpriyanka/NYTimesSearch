package com.underarmour.nytimes.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.underarmour.nytimes.R;
import com.underarmour.nytimes.models.Article;

import org.parceler.Parcels;

public class DetailedArticleActivity extends AppCompatActivity {

    private static final String ARTICLE_TEXT = "article";
    private static final String TAG = DetailedArticleActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private Article article;
    private WebView webView;
    private Toolbar toolbar;
    private ShareActionProvider miShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_article);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        article = (Article) Parcels.unwrap(getIntent().getParcelableExtra(ARTICLE_TEXT));
        setView();
    }

    /**
     * initializ all views
     */
    private void setView() {

        progressBar = (ProgressBar) findViewById(R.id.pbProgressAction);
        setProgressView();
        final String webUrl = article.getWebUrl();
        webView = (WebView) findViewById(R.id.wvArticle);
        webView.setWebViewClient(new ArticleBrowser());
        webView.loadUrl(webUrl);
    }

    /**
     * handling callbacks
     */
    private class ArticleBrowser extends WebViewClient{

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            view.loadUrl(request.getUrl().toString());
            hideProgress();
            return super.shouldOverrideUrlLoading(view, request);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            hideProgress();
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

            hideProgress();
            super.onReceivedError(view, request, error);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            hideProgress();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.share_link_menu, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        item.setIcon(R.drawable.ic_share_icon_24dp);
       // miShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // to avoid share history shown on tool bar
        miShareActionProvider = new ShareActionProvider(this){

            @Override
            public View onCreateActionView() {
                return null;
            }
        };
        MenuItemCompat.setActionProvider(item,miShareActionProvider);

        // share intent to share url
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(getString(R.string.share_content_type   ));
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.share_link_msg));
        shareIntent.putExtra(Intent.EXTRA_TEXT,webView.getUrl());

        // attach intent to shareActionProvider
        if(miShareActionProvider!=null)
            miShareActionProvider.setShareIntent(shareIntent);
        // Return true to display menu
        return super.onCreateOptionsMenu(menu);

    }


    /***
     * show progress meanwhile the page is loading
     */
    private void setProgressView(){

        if(progressBar!= null && !progressBar.isShown())
            progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide progress bar after page is loaded
     */
    private void hideProgress(){

        if(progressBar!= null && progressBar.isShown())
            progressBar.setVisibility(View.GONE);
    }
}
