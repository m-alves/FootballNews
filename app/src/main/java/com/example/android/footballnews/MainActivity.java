package com.example.android.footballnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Story>>{

    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/football?api-key=test";

    private static final int STORY_LOADER_ID = 1;

    /** Adapter for the list of news */
    private MyRecyclerViewAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);

        // Create a new adapter that takes an empty list of stories as input
        mAdapter = new MyRecyclerViewAdapter(this, new ArrayList<Story>());

        // Make the RecyclerView use the Adapter created above
        mRecyclerView.setAdapter(mAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(STORY_LOADER_ID, null, this);
        } else {
            // Otherwise, display error and hide loading indicator
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(this, getText(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Loader<List<Story>> onCreateLoader(int i, Bundle bundle) {
        return new StoryLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> stories) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        if (stories != null && !stories.isEmpty()) {
            mAdapter.setStoriesList(stories);
        }

        // Open the story in the browser
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Story story) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(story.getLink()));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        mAdapter.notifyDataSetChanged();
    }
}
