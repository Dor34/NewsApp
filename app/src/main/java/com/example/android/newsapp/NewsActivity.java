package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>> {

    public static final String LOG_TAG = NewsActivity.class.getName();

    /** URL for API Guardian dataset */
    private static final String Guardian_URL =
            "https://content.guardianapis.com/search";

    //Adapter for list of news articles
    private NewsAdapter newsAdapter;

    //Static value for article loader
    private static final int NEWS_LOADER_ID = 1;

    //Empty textview displayed
    private TextView mEmptyState;

    //Progress bar at the start of the app
    private ProgressBar mProgressBar;

    //Refresh screen when top of screen is pulled
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.refresh);

        newsAdapter = new NewsAdapter (this, new ArrayList<NewsData> ());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(newsAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current news article that was clicked on
                NewsData currentNews = newsAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        //Creates empty state view when no data returns
        mEmptyState = (TextView) findViewById (R.id.empty_view);
        newsListView.setEmptyView (mEmptyState);
        checkNetwork();
    }

    private void checkNetwork(){
        //Checks for network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        //Details for current active network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo ();

        //if connected retrieves data
        if (networkInfo != null && networkInfo.isConnected ()){
            LoaderManager loaderManager = getLoaderManager ();
            loaderManager.initLoader (NEWS_LOADER_ID, null, this);
        }else {
            mProgressBar = (ProgressBar) findViewById (R.id.progress_bar);
            mProgressBar.setVisibility (View.GONE);
            mEmptyState.setText (R.string.no_network_connect);
        }
    }

    @Override
    public Loader<List<NewsData>> onCreateLoader(int id, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences (this);

        String orderBy = sharedPrefs.getString (
                getString (R.string.settings_order_by_key),
                getString (R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(Guardian_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", "search");
        uriBuilder.appendQueryParameter("orderby", orderBy);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("pageSize", "20");
        uriBuilder.appendQueryParameter ("api-key", "8a09433d-9465-45cf-bdef-cc24b13a0e45");

        Log.e(LOG_TAG, uriBuilder.toString ());
        return new NewsLoader (this, uriBuilder.toString ());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsData>> loader, List <NewsData> news) {
        //progress is hidden when article is done loading
        mProgressBar = findViewById (R.id.progress_bar);
        mProgressBar.setVisibility (View.GONE);

        //When no articles are found
        mEmptyState.setText (R.string.no_articles);
        newsAdapter.clear ();

        //if there are articles will add them to adapter and update listview
        if (news != null && !news.isEmpty ()){
            newsAdapter.addAll (news);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsData>> loader) {
        Log.i(LOG_TAG, "onLoaderReset() called");
        newsAdapter.clear ();
    }

    @Override
    //Initializes contents of activity settings menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater ().inflate (R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId ();
        if (id == R.id.action_settings){
            Intent settngsIntent = new Intent(this, NewsSettings.class);
            startActivity (settngsIntent);
            return true;
        }
        return super.onOptionsItemSelected (item);
    }
}
