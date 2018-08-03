package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsData>> {

    /** Query URL **/
    private String mQueryUrl;

    /** {@link NewsLoader} constructor.
     *
     * @param context of the activity
     * @param queryUrl to load data
     */
    public NewsLoader(Context context, String queryUrl) {
        super(context);
        mQueryUrl = queryUrl;
    }

    /**
     * Starts the loading of data in a background thread.
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This performs on a background thread. It fetches the Article data from {@link QueryUtils}
     * and returns the data.
     */
    @Override
    public List<NewsData> loadInBackground() {
        if (mQueryUrl == null) {
            return null;
        }
        // Calls the {@link QueryUtils} fetchArticleData() method to send network request,
        // parse its response and extract the data.
        List<NewsData> articles = QueryUtils.fetchNewsData(mQueryUrl);
        return articles;
    }
}
