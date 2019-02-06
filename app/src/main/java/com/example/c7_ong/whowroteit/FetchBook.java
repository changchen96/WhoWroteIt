package com.example.c7_ong.whowroteit;

import android.os.AsyncTask;
import android.util.EventLogTags;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class FetchBook extends AsyncTask<String, Void, String> {
    //WeakReference is used to prevent memory leaks as it will be deleted by Java's own garbage collection feature when not needed
    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;
    private WeakReference<TextView> Description;

    public FetchBook(TextView TitleText, TextView AuthorText, TextView Description)
    {
        this.mTitleText = new WeakReference<>(TitleText);
        this.mAuthorText = new WeakReference<>(AuthorText);
        this.Description = new WeakReference<>(Description);
    }
    @Override
    protected String doInBackground(String... strings) {
        //returns value from the function in the NetworkUtils class
        return NetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try{
            //used to obtain the JSON array of items from the result string
            //convert the response to a JSON object
            JSONObject jsonObject = new JSONObject(s);
            //get the JSON array for book items
            JSONArray itemsArray = jsonObject.getJSONArray("items");
            //initialize iterator and results fields
            int i = 0;
            String title = null;
            String authors = null;
            String description = null;
            while (i < itemsArray.length() && (authors == null && title == null))
            {
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                //try and get the author and the title from the current item
                //catch if either field is empty
                try
                {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                    description = volumeInfo.getString("description");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                //increment the value of i to move to the next item
                i++;
            }

            if (title != null && authors != null)
            {
                //if a response is found, update the UI with the response
                mTitleText.get().setText(title);
                mAuthorText.get().setText(authors);
                Description.get().setText(description);

            }
            else
            {
                //if the result has no value author and a title
                mTitleText.get().setText(R.string.no_results);
                mAuthorText.get().setText("");
                Description.get().setText("");

            }
        }
        catch (JSONException e)
        {
            //If onPostExecute does not get a proper JSON string, update the UI to show failed results
            mTitleText.get().setText(R.string.no_results);
            mAuthorText.get().setText("");
            Description.get().setText("");
            e.printStackTrace();
        }
    }

}
