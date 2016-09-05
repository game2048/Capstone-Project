//package com.example.android.expnesify;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
//import android.util.Log;
//import android.support.v4.app.Fragment;
//
//import com.example.android.expnesify.data.ExpenseContract;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//
///**
// * Created by vaibhav.seth on 8/21/16.
// */
//public class graph extends Fragment {
//
//    Uri uri;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_line_graph2);
//        Intent i = getIntent();
//        String sym = i.getStringExtra("symbol");
//
//        DbTask asyncTaskForLineGraph = new DbTask();
//        asyncTaskForLineGraph.execute(uri.toString());
//    }
//
//    public class DbTask extends AsyncTask<String, Void, Void> {
//        private final Context mContext;
//
//        public DbTask(Context context) {
//            mContext = context;
//        }
//
//        @Override
//        protected Void doInBackground(String... params) {
//            ContentValues locationValues = new ContentValues();
//            if(params[0].equals("Update")) {
//                // Then add the data, along with the corresponding name of the data type,
//                // so the content provider knows what kind of value is being inserted.
//                locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_AMOUNT, params[2]);
//                locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_CATEGORY, params[3]);
//                locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_DATE, params[4]);
//                locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_NOTE, params[5]);
//
//                // Finally, insert location data into the database.
//                int insertedUri = mContext.getContentResolver().update(
//                        ExpenseContract.ExpenseEntry.CONTENT_URI,
//                        locationValues, ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_ID+ "=?", new String[]{params[1]}
//                );
//
//                // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
////            long expenseId = ContentUris.parseId(insertedUri);
//                System.out.println("Expense Saved !!" + insertedUri);
//            }
//            else
//            {
//                int deletedUri = mContext.getContentResolver().delete(
//                        ExpenseContract.ExpenseEntry.CONTENT_URI,
//                        ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_ID+ "=?", new String[]{params[1]}
//                );
//                System.out.println("Deleted!!" + deletedUri);
//            }
//            return null;
//        }
//    }
//
////    class AsyncTaskForLineGraph extends AsyncTask<String,String,String> {
////
////        @Override
////        protected String doInBackground(String... params) {
////
////
////            return null;
////        }
////
////        @Override
////        protected void onPostExecute(String s) {
////            int animateSeconds = 1000;
////            super.onPostExecute(s);
////            LineDataSet dataset = new LineDataSet(entries, "Stock Values over time");
////            dataset.setDrawCircles(true);
////            dataset.setDrawValues(true);
////            LineData data = new LineData(labels,dataset);
////            lineChart.setDescription("Graph for Stock Values");
////            lineChart.setData(data);
////            lineChart.animateY(animateSeconds);
////
////        }
////    }
//}
//
