package com.josikie.b.battlebooks;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BooksAdapter extends ArrayAdapter<Books> {

    public BooksAdapter(Activity context, ArrayList<Books> booksArrayList) {
        super(context, 0, booksArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.books_list_items, parent, false);
        }
        Books books = getItem(position);

        ImageView imageView = view.findViewById(R.id.img_books);
        Glide.with(getContext()).load(books.getImage()).into(imageView);


        TextView booksTitle = view.findViewById(R.id.books_title);
        booksTitle.setText(books.getBookTitle());

        TextView authorName = view.findViewById(R.id.author_name);
        authorName.setText(books.getAuthorName());

        return view;

    }


}
