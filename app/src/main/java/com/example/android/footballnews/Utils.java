package com.example.android.footballnews;

/**
 * Created by Utilizador on 05/08/2017.
 */

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
 * Helper methods related to requesting and receiving story data from the Guardian API.
 */
public class Utils {

    /** Tag for the log messages */
    private static final String LOG_TAG = Utils.class.getSimpleName();

    /*Set ReadTimeout constant*/
    private static final int READ_TIMEOUT = 10000;

    /*Set ConnectTimeout constant*/
    private static final int CONNECT_TIMEOUT = 15000;

    /*Successful Response Code*/
    private static final int SUCCESSFUL_RESPONSE_CODE = 200;



    /**
     * Create a private constructor
     */
    private Utils(){}

    /**
     * Query the Guardian dataset and return a list of Story objects.
     */
    public static List<Story> fetchStoryData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of Stories
        List<Story> stories = extractFeatureFromJson(jsonResponse);
        // Return the list of stories
        return stories;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == SUCCESSFUL_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving stories results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the InputStream into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of Story objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Story> extractFeatureFromJson(String storyJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(storyJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding stories to
        List<Story> stories = new ArrayList<>();
        // Try to parse the JSON response string.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJson = new JSONObject(storyJSON);

            // Extract the response JSON object
            JSONObject baseJsonResponse = baseJson.getJSONObject("response");

            //Extract the results JSON array, where each result is a story.

            JSONArray storyArray = baseJsonResponse.getJSONArray("results");

            // For each story in the storyArray, create an Story object
            for (int i = 0; i < storyArray.length(); i++) {
                // Get a single story at position i within the list of stories
                JSONObject currentStory = storyArray.getJSONObject(i);

                // Extract the components of interest
                String webTitle = currentStory.getString("webTitle");
                String storyAuthor = "";
                String separator = "|";
                if (webTitle.contains(separator)){
                    // define webTitle
                    int separatorIndex = webTitle.indexOf(separator);
                    // define author
                    storyAuthor = webTitle.substring(separatorIndex+2, webTitle.length()-1);
                    webTitle = webTitle.substring(0, separatorIndex);
                } else {
                    storyAuthor = "No author available";
                }
                String sectionName = currentStory.getString("sectionName");
                String link = currentStory.getString("webUrl");
                String date = currentStory.getString("webPublicationDate");
                if(!date.equals("")){
                    date = date.substring(0,10);
                } else {
                    date = "No date available";
                }
                // Create a new Story object with the components extracted above
                Story story = new Story(webTitle, sectionName, storyAuthor, date, link  );
                // Add the new story to the stories array.
                stories.add(story);
            }
        } catch (JSONException e) {

            Log.e("Utils", "Problem parsing the Story JSON results", e);
        }
        // Return the list of stories
        return stories;
    }
}
