package com.example.android.expnesify;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {
    static SharedPreferences prefs ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String value = prefs.getString(getString(R.string.pref_metric_key),
                String.valueOf(R.string.pref_default));
        final Context context = this;

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.expense_widget);
                ComponentName thisWidget = new ComponentName(context, ExpenseWidget.class);
                remoteViews.setTextViewText(R.id.appwidget_text, getString(R.string.appwidget_text) + value + "$");
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);
            }
        };

        prefs.registerOnSharedPreferenceChangeListener(listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_export) {
            Intent intent1 = new Intent(this,exportData.class);
            this.startActivity(intent1);
            return true;
        }

        if (id == R.id.action_history) {
            return true;
        }
        if (id == R.id.action_statistics) {
            Intent intent1 = new Intent(this,statistics.class);
            this.startActivity(intent1);
            return true;
        }

        if (id == R.id.newExpense) {
            Intent intent1 = new Intent(this,NewExpense.class);
            this.startActivity(intent1);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
