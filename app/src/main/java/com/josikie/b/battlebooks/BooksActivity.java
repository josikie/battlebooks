package com.josikie.b.battlebooks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {
    private final String LOGT = BooksActivity.class.getSimpleName();
    private String baseURL1 = "https://www.googleapis.com/books/v1/volumes?q=battle&maxResults=20";

    private String baseURL = "https://www.googleapis.com/books/v1/volumes?q=battle&maxResults=20";

    private String searchURL = "https://www.googleapis.com/books/v1/volumes?q=battle+intitle:";

    private BooksAdapter booksAdapter;

    private ProgressBar progressBar;

    private TextView emptyStatesLayout;
    private TextView needRefresh;

    private EditText editText;

    private String search;

    ListView booksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // progress bar
        progressBar = findViewById(R.id.progresBar);

        // refresh layout
        needRefresh = findViewById(R.id.need_refresh);

        // list view
        booksListView = findViewById(R.id.list_books);

        // if empty
        emptyStatesLayout = findViewById(R.id.empty_text_for_layout);

        // set empty view
        booksListView.setEmptyView(emptyStatesLayout);
        booksListView.setEmptyView(needRefresh);

        booksAdapter = new BooksAdapter(this, new ArrayList<Books>());

        booksListView.setAdapter(booksAdapter);


        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(1, null, this);

         editText = findViewById(R.id.search_text_field);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Books books = booksAdapter.getItem(i);

                Uri uri = Uri.parse(books.getUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public Loader<List<Books>> onCreateLoader(int id, @Nullable Bundle args) {
        return new BooksLoader(this, baseURL);

    }


    @Override
    public void onLoadFinished(@NonNull Loader<List<Books>> loader, List<Books> data) {
        booksAdapter.clear();
        progressBar.setVisibility(View.GONE);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (data != null && !data.isEmpty()) {
            booksAdapter.addAll(data);
        }else if (networkInfo == null || !networkInfo.isConnected()) {
            progressBar.setVisibility(View.GONE);
            emptyStatesLayout.setText(R.string.no_internet);
        }else {
            progressBar.setVisibility(View.GONE);
            needRefresh.setText(R.string.refresh);
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader<List<Books>> loader) {
        booksAdapter.clear();
    }

    public void submitSearch(View view){
        search = editText.getText().toString();
        baseURL = searchURL + search;
        getSupportLoaderManager().restartLoader(1, null, this);
        baseURL = baseURL1;
    }
}