package com.example.android.newsapp;

public class NewsData {

    //Title of article
    private String mTitle;

    // Source of article
    private String mSource;

    //Section of news article
    private String mSection;

    // Date of article
    private String mDate;

    //URL of news article
    private String mUrl;

    public NewsData(String title, String source, String date, String section, String url) {

        mTitle = title;
        mSource = source;
        mSection = section;
        mDate = date;
        mUrl = url;
    }

    /**
     * Title of article
     */
    public String getTitle(){
        return mTitle;
    }

    /**
     * Source of article
     */
    public String getSource() {
        return mSource;
    }

    /**
     * Article section
     */
    public String getSection(){
        return mSection;
    }

    /**
     * Date article was written
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Url to website for more information about the article
     */
    public String getUrl(){
        return mUrl;
    }

}
