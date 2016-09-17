package com.example.android.expnesify;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.expnesify.data.ExpenseContract;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class statisticsFragment extends Fragment {
    BarChart lineChart;

    public statisticsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        lineChart = (BarChart) rootView.findViewById(R.id.linechart);

        DbTask d = new DbTask(getActivity());
        d.execute();
        return rootView;
    }

    class DbTask extends AsyncTask<String, Void, String> {
        private final Context mContext;
        ArrayList<BarDataSet> entries = null;
        ArrayList<String> labels = new ArrayList<String>();

        public DbTask(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... params) {

            String[] FORECAST_COLUMNS = {
                    // In this case the id needs to be fully qualified with a table name, since
                    // the content provider joins the location & weather tables in the background
                    // (both have an _id column)
                    // On the one hand, that's annoying.  On the other, you can search the weather table
                    // using the location set by the user, which is only in the Location table.
                    // So the convenience is worth it.

                    ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_CATEGORY,
                    "SUM(expense_amount)"
            };
            int i =0;
            String[] sel = new String[]{"strftime('%m',date('now'))"};
            // Finally, insert location data into the database.
            Cursor selectedURI = mContext.getContentResolver().query(ExpenseContract.ExpenseEntry.CONTENT_URI,
                    FORECAST_COLUMNS,
                    "strftime('%m', expense_date) = strftime('%m',date('now')) group by expense_category", null, null
            );
            ArrayList<BarEntry> valueSet1 = new ArrayList<>();
            ArrayList<BarEntry> valueSet2 = new ArrayList<>();
            ArrayList<BarEntry> valueSet3 = new ArrayList<>();
            ArrayList<BarEntry> valueSet4 = new ArrayList<>();
            ArrayList<BarEntry> valueSet5 = new ArrayList<>();
            while (selectedURI.moveToNext()) {
                if(selectedURI.getString(0).equals("Food and Drinks")) {
                    BarEntry v1e1 = new BarEntry(Float.parseFloat(selectedURI.getString(1)), i);
                    valueSet1.add(v1e1);
                }
                else if(selectedURI.getString(0).equals("Health")) {
                    BarEntry v2e1 = new BarEntry(Float.parseFloat(selectedURI.getString(1)), i);
                    valueSet2.add(v2e1);
                }
                else if(selectedURI.getString(0).equals("Transportation")) {
                    BarEntry v2e1 = new BarEntry(Float.parseFloat(selectedURI.getString(1)), i);
                    valueSet3.add(v2e1);
                }
                else if(selectedURI.getString(0).equals("Leisure")) {
                    BarEntry v2e1 = new BarEntry(Float.parseFloat(selectedURI.getString(1)), i);
                    valueSet4.add(v2e1);
                }
                else if(selectedURI.getString(0).equals("Others")) {
                    BarEntry v2e1 = new BarEntry(Float.parseFloat(selectedURI.getString(1)), i);
                    valueSet5.add(v2e1);
                }
            }
            labels.add("Data");
            BarDataSet barDataSet1 = new BarDataSet(valueSet1, getString(R.string.foodDrinks));
            barDataSet1.setColor(Color.rgb(0, 155, 0));
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, getString(R.string.health));
            barDataSet2.setColor(Color.rgb(193, 37, 82));
            BarDataSet barDataSet3 = new BarDataSet(valueSet3, getString(R.string.transportation));
            barDataSet3.setColor(Color.rgb(255, 102, 0));
            BarDataSet barDataSet4 = new BarDataSet(valueSet4, getString(R.string.leisure));
            barDataSet4.setColor(Color.rgb(245, 199, 155));
            BarDataSet barDataSet5 = new BarDataSet(valueSet5, getString(R.string.others));
            barDataSet5.setColor(Color.rgb(179, 155, 53));

            entries = new ArrayList<>();
            entries.add(barDataSet1);
            entries.add(barDataSet2);
            entries.add(barDataSet3);
            entries.add(barDataSet4);
            entries.add(barDataSet5);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            int animateSeconds = 2000;
            super.onPostExecute(s);

            BarData data = new BarData(labels,  entries);
            lineChart.setDescription(getString(R.string.graph));
            lineChart.setData(data);
            lineChart.animateXY(animateSeconds, animateSeconds);
            lineChart.invalidate();
        }
    }
}
