package com.example.c7_ong.whowroteit;

import android.os.AsyncTask;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class FetchBook extends AsyncTask<String, Void, String> {
    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;

    public FetchBook(TextView TitleText, TextView AuthorText)
    {
        this.mTitleText = new WeakReference<>(TitleText);
        this.mAuthorText = new WeakReference<>(AuthorText);
    }
    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}
