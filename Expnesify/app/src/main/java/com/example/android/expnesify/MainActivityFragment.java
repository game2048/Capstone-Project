package com.example.android.expnesify;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.expnesify.data.ExpenseContract;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_new_expense, container, false);
        final List<String> a = new ArrayList<String>();

        final TextView amount = (TextView) rootView.findViewById(
                R.id.amountText);
//        final TextView date = (TextView) rootView.findViewById(
//                R.id.date);
        final EditText fromDateEtxt = (EditText) rootView.findViewById(R.id.etxt_fromdate1);
        final TextView note = (TextView) rootView.findViewById(
                R.id.note);
        final RadioGroup category = (RadioGroup) rootView.findViewById(
                R.id.radioCateogry);


        final Button cancelBtn = (Button) rootView.findViewById(
                R.id.cancel);
        final Button saveBtn = (Button) rootView.findViewById(
                R.id.save
        );
//            ContentValues values = new ContentValues();
//            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAV,"True");
//            int insertedUri = mContext.getContentResolver().update(MovieContract.MovieEntry.buildMovieUri(),values,MovieContract.MovieEntry.COLUMN_MOVIE_ID + "?",new String[] {String.valueOf(movieId)});

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if( amount.getText().toString().trim().equals("")){

                    amount.setError(getString(R.string.amountError));
                    Toast.makeText(getActivity(), getString(R.string.amountError), Toast.LENGTH_SHORT).show();
                }
                else if(fromDateEtxt.getText().toString().trim().equals("")) {
                    fromDateEtxt.setError(getString(R.string.dateError));
                    Toast.makeText(getActivity(), getString(R.string.dateError), Toast.LENGTH_SHORT).show();
                }
                else if(category.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), getString(R.string.catError), Toast.LENGTH_SHORT).show();
                }
                else {


                    a.add(amount.getText().toString());
                    a.add(((RadioButton) rootView.findViewById(category.getCheckedRadioButtonId())).getText().toString());
                    a.add(fromDateEtxt.getText().toString());
                    a.add(note.getText().toString());
                    InsertDbTask expenseInsertTask = new InsertDbTask(getActivity());
                    expenseInsertTask.execute(a.toArray(new String[a.size()]));
                    Toast.makeText(getActivity(), "save", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InsertDbTask movieUpdateTask = new InsertDbTask(getActivity());
                System.out.println("Nothing saved !!");
                Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public class InsertDbTask extends AsyncTask<String, Void, Void> {
        private final Context mContext;

        public InsertDbTask(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            ContentValues locationValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_AMOUNT, params[0]);
            locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_CATEGORY, params[1]);
            locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_DATE, params[2]);
            locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_NOTE, params[3]);

            // Finally, insert location data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    ExpenseContract.ExpenseEntry.CONTENT_URI,
                    locationValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            long expenseId = ContentUris.parseId(insertedUri);
            System.out.println("Expense Saved !!" +expenseId);
            return null;
        }
    }
}
