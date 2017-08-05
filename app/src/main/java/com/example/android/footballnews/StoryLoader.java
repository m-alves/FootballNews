package com.example.android.footballnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Utilizador on 05/08/2017.
 */

public class StoryLoader extends AsyncTaskLoader<List<Story>> {

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new BookLoader.
     * @param context of the activity
     * @param url to load data from
     */
    public StoryLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Story> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of books.
        return Utils.fetchStoryData(mUrl);
    }
}

