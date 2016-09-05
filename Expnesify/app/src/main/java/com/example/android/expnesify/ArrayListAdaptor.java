package com.example.android.expnesify;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ArrayListAdaptor extends CursorAdapter {

        private Context context;
        private LayoutInflater inflater;

        private ArrayList<String> imageUrls;

        public ArrayListAdaptor(Context context, Cursor c, int flags) {
            super(context, c, flags);
//        inflater = LayoutInflater.from(context);
        }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (null == convertView) {
//            convertView = LayoutInflater.from(getContext()).inflate(
//                    R.layout.list_item_image, parent, false);
////                    inflater.inflate(R.layout.list_item_image, parent, false);
//        }
//        Movie movie = getItem(position);
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.List_item_image);
//
//        System.out.println("Hello" + position);
//        System.out.println(movie);
//        Picasso
//                .with(context)
//                .load(movie.PosterPath)
////                .fit() // will explain later
//                .into((ImageView) imageView);
//
//        return convertView;
//    }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = (View) LayoutInflater.from(context).inflate(R.layout.expense_text, parent, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView category = (TextView) view.findViewById(R.id.textView3);
            TextView amt = (TextView) view.findViewById(R.id.textView2);
            TextView note = (TextView) view.findViewById(R.id.textView4);
            // Extract properties from cursor
            String body = cursor.getString(2);

            // Populate fields with extracted properties
            category.setText(body);
            amt.setText(cursor.getString(1) + "$");
            note.setText(cursor.getString(4) + " on " + cursor.getString(3));
        }

    }
