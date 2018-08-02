package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>> {

    public static final String LOG_TAG = NewsActivity.class.getName();

    /** URL for earthquake data from The Guardian dataset */
    private static final String Guardian_URL =
            "https://www.theguardian.com/world/2018/aug/01/zanu-pf-wins-majority-of-seats-in-zimbabwe-parliament-elections";

    //Adapter for list of news articles
    private NewsAdapter mNewsAdapter;

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

        final NewsAdapter mAdapter = new NewsAdapter (this, new ArrayList<NewsData> ());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current news article that was clicked on
                NewsData currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing (true);
                checkNetwork();
                (new Handler ()).postDelayed (new Runnable () {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing (false);
                    }
                }, 3000);
            }
        });

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
        return new NewsLoader (this, Guardian_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsData>> loader, List <NewsData> news) {
        //progress is hidden when article is done loading
        mProgressBar = (ProgressBar) findViewById (R.id.progress_bar);
        mProgressBar.setVisibility (View.GONE);

        //When no articles are found
        mEmptyState.setText (R.string.no_articles);
        //Clears previous data
        mNewsAdapter.clear ();

        //if there articles will addt them to adapter and update listview
        if (news != null && !news.isEmpty ()){
            mNewsAdapter.addAll (news);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsData>> loader) {
        Log.i(LOG_TAG, "onLoaderReset() called");
        mNewsAdapter.clear ();
    }
}
