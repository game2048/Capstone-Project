package com.example.android.expnesify;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.expnesify.data.ExpenseContract;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class ExpenseList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public ExpenseList() {
    }
    ArrayListAdaptor adapter = null;
    public static final int EXPENSE_LOADER = 0;
    SharedPreferences prefs ;
    String sumExpense = "0";
    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            ExpenseContract.ExpenseEntry.TABLE_NAME + "." + ExpenseContract.ExpenseEntry._ID,
            ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_AMOUNT,
            ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_CATEGORY,
            ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_DATE,
            ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_NOTE
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);


//        return new R.layout.list_item_forecast;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expense_list, container, false);
//        View emptyView = inflater.inflate(R.layout.empty_list, container, false);


//        final Button saveBtn = (Button) rootView.findViewById(
//                R.id.newExpense);
//        saveBtn.setOnClickListener(new View.OnClickListener() {
//
////            @Override
////            public void onClick(View v) {
////                Toast.makeText(getActivity(), "button", Toast.LENGTH_SHORT).show();
////                Intent intent = new Intent(getActivity(), NewExpense.class);
////                    startActivity(intent);
////
////            }
////        });


        adapter = new ArrayListAdaptor(getActivity(),
                null,0);
//        mImage = new ImageListAdapter(getActivity(), null, 0);
        //mForecastAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,new ArrayList<String>());
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setEmptyView((TextView)rootView.findViewById(R.id.empty));
//        TextView emptyTextView = (TextView) rootView.findViewById(R.id.empty);
//        if(adapter.getCount() > 0 ) {
//            emptyTextView.setVisibility(View.INVISIBLE);
//            listView.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            emptyTextView.setVisibility(View.VISIBLE);
//            listView.setVisibility(View.INVISIBLE);
//        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), EditExpense.class);
//                            .setData(ExpenseContract.ExpenseEntry.buildExpenseUri(cursor.getString(1))
//                            );
                    intent.putExtra("id", cursor.getString(0));
                    intent.putExtra("amount", cursor.getString(1));
                    intent.putExtra("category", cursor.getString(2));
                    intent.putExtra("date", cursor.getString(3));
                    intent.putExtra("note", cursor.getString(4));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(EXPENSE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
//        updateWeather();
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
//        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri movieUri =  null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String units = prefs.getString(getString(R.string.pref_metric_key),
//                getString(R.string.pref_location_default));
//        if(units.equalsIgnoreCase("most popular")|| units.equalsIgnoreCase("highest-rated")) {
            movieUri = ExpenseContract.ExpenseEntry.buildExpenseUri();
//        }
//        else {
//            movieUri = MovieContract.MovieEntry.buildFavMovieUri();
//        }
        String[] monthData = {
                // In this case the id needs to be fully qualified with a table name, since
                // the content provider joins the location & weather tables in the background
                // (both have an _id column)
                // On the one hand, that's annoying.  On the other, you can search the weather table
                // using the location set by the user, which is only in the Location table.
                // So the convenience is worth it.
                "strftime('%m', expense_date) as month",
                "SUM(expense_amount)"
        };
        int i =0;
        String[] sel = new String[]{"strftime('%m',date('now'))"};
        // Finally, insert location data into the database.
        Cursor selectedURI = getActivity().getContentResolver().query(ExpenseContract.ExpenseEntry.CONTENT_URI,
                monthData,
                "strftime('%m', expense_date) = strftime('%m',date('now')) group by month", null, null
        );
        while(selectedURI.moveToNext())
        {
            sumExpense=selectedURI.getString(1);
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.getString(getString(R.string.pref_metric_key),
                getString(R.string.pref_default));
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(getString(R.string.pref_metric_key), sumExpense);
        editor.commit();

        TextView t= (TextView) getActivity().findViewById(R.id.textViewAll);
        t.setText(getString(R.string.textAll) + " :" + sumExpense + "$");


        CursorLoader expenseList = new CursorLoader(getActivity(),
                movieUri,
                FORECAST_COLUMNS,
                null,
                null,
                null);
        return expenseList;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
