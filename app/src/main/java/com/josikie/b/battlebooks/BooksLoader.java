package com.josikie.b.battlebooks;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class BooksLoader extends AsyncTaskLoader<List<Books>> {

    private static final String LOG_TAG = BooksLoader.class.getSimpleName();
    String url;
    public BooksLoader(@NonNull Context context, String urls) {
        super(context);
        url = urls;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST : onStartLoading is called");
        forceLoad();
    }

    @Nullable
    @Override
    public List<Books> loadInBackground() {
        Log.i(LOG_TAG, "TEST : loadInBackground method is calling");
        if (url == null) {
            return null;
        }
        List<Books> booksList = QueryUtils.fetchBooksData(url);
        return booksList;
    }
}
