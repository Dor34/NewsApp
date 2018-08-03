package com.example.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<NewsData> {

    public NewsAdapter(Activity context, List<NewsData> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        //checks for reused view = true otherwise inflate
        if (convertView == null){
            listItemView = LayoutInflater.from (getContext ()).inflate
                    (R.layout.news_list_item, parent, false);
        }
        NewsData currentNews = getItem (position);

        //Article title
        TextView titleView = listItemView.findViewById (R.id.title);
        titleView.setText (currentNews.getTitle());

        //Article source
        TextView contributorView = listItemView.findViewById (R.id.contributor);
        contributorView.setText (currentNews.getContributor());

        //Article section
        TextView sectionView = listItemView.findViewById (R.id.section);
        sectionView.setText (currentNews.getSection ());

        SimpleDateFormat newDate = new SimpleDateFormat ("yyyy-MM-dd");

        //Date of article
        TextView dateView = listItemView.findViewById (R.id.date);
        //Date format string ("December 31, 2018")
        return listItemView;
        }

    private String formatDate() {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM--dd");
        Date date = new Date ();
        return dateFormat.format(date);
    }
}
