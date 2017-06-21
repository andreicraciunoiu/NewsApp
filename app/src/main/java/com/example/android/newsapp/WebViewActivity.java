package com.example.android.newsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView webView = (WebView)findViewById(R.id.web_view);
        Intent i = getIntent();
        String url = i.getData().toString();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.web_progress_bar);
        webView.setWebViewClient(new ArticleWebView(progressBar));
        webView.loadUrl(url);
    }
}