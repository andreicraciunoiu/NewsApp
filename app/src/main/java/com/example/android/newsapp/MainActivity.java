package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LINK = "https://content.guardianapis.com/search";
    private static final String API_KEY = "d3d0923c-555d-4e78-a3a7-809e2e043a4b";

    private RecyclerView recyclerView;
    private ArticleRecyclerAdapter recyclerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView emptyState = (TextView) findViewById(R.id.error_text_view);
        recyclerView = (RecyclerView)findViewById(R.id.list_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        final boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if(isConnected){
            emptyState.setVisibility(View.GONE);
            recyclerAdapter = new ArticleRecyclerAdapter(MainActivity.this, new ArrayList<Article>());
            recyclerView.setAdapter(recyclerAdapter);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, MainActivity.this);
        }else {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            emptyState.setText(getString(R.string.noInternet));
            emptyState.setVisibility(View.VISIBLE);
        }
    }

    public void startWebView(String url) {
        Intent webIntent = new Intent(MainActivity.this, WebViewActivity.class);
        webIntent.setData(Uri.parse(url));
        startActivity(webIntent);
    }

    private static class ArticleLoader extends AsyncTaskLoader<List<Article>> {

        private final String url;

        ArticleLoader(Context context, String url){
            super(context);
            this.url = url;
        }

        @Override
        protected  void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<Article> loadInBackground() {
            if(url == null){
                return null;
            }
            return QueryUtils.fetchNewsData(url);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String section = preferences.getString(getString(R.string.section), getString(R.string.section));
        String listSize = preferences.getString(getString(R.string.listSize), getString(R.string.listSizeDefault));

        Uri baseUri = Uri.parse(LINK);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", section);
        uriBuilder.appendQueryParameter("page-size", listSize);
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Article>> loader, List<Article> data) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerAdapter = new ArticleRecyclerAdapter(MainActivity.this, new ArrayList<Article>());

        if(data != null && !data.isEmpty()){
            recyclerAdapter = new ArticleRecyclerAdapter(MainActivity.this, data);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Article>> loader) {
        recyclerAdapter = new ArticleRecyclerAdapter(MainActivity.this, new ArrayList<Article>());
    }
}