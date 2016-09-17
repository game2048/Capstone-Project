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


        public ArrayListAdaptor(Context context, Cursor c, int flags) {
            super(context, c, flags);
//        inflater = LayoutInflater.from(context);
        }


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
            String body = cursor.getString(2);

            category.setText(body);
            amt.setText(cursor.getString(1) + context.getString(R.string.dollar));
            note.setText(cursor.getString(4) + context.getString(R.string.on) + cursor.getString(3));
        }

    }
