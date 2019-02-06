package com.example.c7_ong.whowroteit;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    //constants for the API
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final String QUERY_PARAM = "q";
    private static final String MAX_RESULTS = "maxResults";
    private static final String PRINT_TYPE = "printType";
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    static String getBookInfo (String queryString)
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;


        try{
            //request URI for the API
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();
            //URI is converted into a URL object
            URL requestURL = new URL (builtURI.toString());

            //opens the URL connection and sets the request
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Get the input stream from the connection
            InputStream inputStream = urlConnection.getInputStream();

            //Buffered reader for the input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));

            //Used to hold the incoming response
            StringBuilder builder = new StringBuilder();

            //the input is read line-by-line to the string while there is still input
            String line;
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
                builder.append("\n");
                if (builder.length() == 0)
                {
                    //if stream is empty, return null
                    return null;
                }
            }
            bookJSONString = builder.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            //closes both the connection and the BufferedReader
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if (reader!=null)
            {
                try
                {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG, bookJSONString);
        return bookJSONString;
    }
}
