package com.example.android.newsapp;

public class NewsData {

    //Title of article
    private String mTitle;

    // Source of article
    private String mContributor;

    //Section of news article
    private String mSection;

    // Date of article
    private String mDate;

    //URL of news article
    private String mUrl;

    public NewsData(String title, String contributor, String date, String section, String url) {

        mTitle = title;
        mContributor = contributor;
        mSection = section;
        mDate = date;
        mUrl = url;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getContributor() {
        return mContributor;
    }

    public String getSection(){
        return mSection;
    }

    public String getDate() {
        return mDate;
    }
    
    public String getUrl(){
        return mUrl;
    }

}
