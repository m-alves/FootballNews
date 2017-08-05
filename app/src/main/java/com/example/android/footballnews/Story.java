package com.example.android.footballnews;

/**
 * Created by Utilizador on 05/08/2017.
 */

public class Story {

    private String mTitle;

    private String mSection;

    private String mAuthor;

    private String mDate;

    private String mLink;

    public Story(String title, String section, String author, String date, String link){
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = date;
        mLink = link;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }

    public String getLink() {
        return mLink;
    }
}
