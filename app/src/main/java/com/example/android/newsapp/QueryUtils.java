package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Methods for requesting, receiving,and parsing article data from The Guardian API.
 */
public class QueryUtils {

    /** Tag for the log message */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName ();

    /**
     * A private constructor because the methods and varibles are static.
     */
    private QueryUtils() {
    }

    public static List<NewsData> fetchNewsData(String requestUrl){
        URL url = createUrl(requestUrl);
        //calls the makeHttpRequest url and gets a JSON response
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e (LOG_TAG, "Problem making HTTP request.", e);
        }

        List<NewsData> newsData = extractFeatureFromJson(jsonResponse);
        return newsData;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e (LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection ();
            urlConnection.setReadTimeout (10000 /*milliseconds */);
            urlConnection.setConnectTimeout (15000 /*milliseconds */);
            urlConnection.connect ();

            //If successful the InputStream is called and response is parsed
            if (urlConnection.getResponseCode () == HttpURLConnection.HTTP_OK){
                inputStream = urlConnection.getInputStream ();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e (LOG_TAG, "Error response code: " + urlConnection.getResponseCode ());
            }
        }catch (IOException e){
            Log.e (LOG_TAG, "Problem retrieving the news JSON results.", e);
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect ();
            }
            if (inputStream != null){
                inputStream.close ();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder ();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader (inputStream, Charset.forName ("UTF-8"));
            BufferedReader reader = new BufferedReader (inputStreamReader);
            String line = reader.readLine ();
            while (line != null){
                output.append (line);
                line = reader.readLine ();
            }
        }
        return output.toString ();
    }

    private static List <NewsData> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty (newsJSON)){
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<NewsData> news = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        try {
            // build up a list of NewsData objects with the corresponding data.
            JSONObject baseJsonResponse = new JSONObject (newsJSON);
            JSONObject newsObject = baseJsonResponse.getJSONObject ("response");
            JSONArray results = newsObject.getJSONArray ("results");

            for (int i = 0; i<results.length (); i++){
                JSONObject currentNewsData = results.getJSONObject (i);

                // Extract the value for the key called "title"
                String title = currentNewsData.getString("webTitle");

                //Extract the for the key called "sectionName"
                String sectionName = currentNewsData.getString ("sectionName");

                //Extract the value for the key called "date"
                String date = currentNewsData.getString ("webPublicationDate");

                //extract value key called "url"
                String url = currentNewsData.getString ("webUrl");

                JSONArray tags = currentNewsData.getJSONArray ("tags");
                String source = "";

                if (tags.length () == 0){
                    source = null;
                }else{
                    for (int x = 0; x <tags.length (); x++){
                        JSONObject currentSource = tags.getJSONObject(x);
                        source = currentSource.getString ("webTitle");
                    }
                }
                NewsData newsData = new NewsData (title, source, sectionName, date, url);
                news.add(newsData);
            }
            }catch (JSONException e){
            Log.e ("QueryUtils", "Problem parsing the article JSON results.", e);
        }
        return news;
    }
}



