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

import com.example.android.expnesify.data.ExpenseContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.getIntent;

/**
 * A placeholder fragment containing a simple view.
 */
public class EditExpenseFragment extends Fragment {

    public EditExpenseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_edit_expense, container, false);
        final Intent in = getActivity().getIntent();
        Map<String,Integer> categoryMap = new HashMap<>();
        categoryMap.put("Food and Drinks",0);
        categoryMap.put("Health",1);
        categoryMap.put("Transportation",3);
        categoryMap.put("Leisure",2);
        categoryMap.put("Others",4);


//        String id = getArguments().getString("id");
//        String amt = getArguments().getString("amount");

//        String expense = null;
//
//        TextView tv = (TextView) root.findViewById(R.id.amount);
//        // Extract properties from cursor
//        // Populate fields with extracted properties
//
//
//        if(getArguments() != null) {
//            expense = getArguments().getString("Movie");
//        }
//        tv.setText(expense.toString());

        final Button cancelBtn = (Button) root.findViewById(
                R.id.cancel);
        final Button saveBtn = (Button) root.findViewById(
                R.id.save
        );
        final Button deleteBtn = (Button) root.findViewById(
                R.id.delete
        );
//            ContentValues values = new ContentValues();
//            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAV,"True");
//            int insertedUri = mContext.getContentResolver().update(MovieContract.MovieEntry.buildMovieUri(),values,MovieContract.MovieEntry.COLUMN_MOVIE_ID + "?",new String[] {String.valueOf(movieId)});
        final TextView amount = (TextView) root.findViewById(
                R.id.amountText);
//        final TextView date = (TextView) root.findViewById(
//                R.id.date);
        final TextView note = (TextView) root.findViewById(
                R.id.noteText);
        final EditText fromDateEtxt = (EditText) root.findViewById(R.id.etxt_fromdate);
        final RadioGroup category = (RadioGroup) root.findViewById(
                R.id.radioCateogry);

        amount.setText(in.getStringExtra("amount"));
//        date.setText(in.getStringExtra("date"));
        note.setText(in.getStringExtra("note"));
        fromDateEtxt.setText(in.getStringExtra("date"));
        ((RadioButton)category.getChildAt(categoryMap.get(in.getStringExtra("category")))).setChecked(true);

        final List<String> a = new ArrayList<String>();
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                a.add("Update");
                a.add(in.getStringExtra("id"));
                a.add(amount.getText().toString());
                a.add(((RadioButton) root.findViewById(category.getCheckedRadioButtonId())).getText().toString());
//                a.add(date.getText().toString());
                a.add(fromDateEtxt.getText().toString());
                a.add(note.getText().toString());

                DbTask expenseInsertTask = new DbTask(getActivity());
                expenseInsertTask.execute(a.toArray(new String[a.size()]));
                Toast.makeText(getActivity(), "save", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InsertDbTask movieUpdateTask = new InsertDbTask(getActivity());
                a.add("Delete");
                a.add(in.getStringExtra("id"));
                System.out.println("deleted !!");
                DbTask expenseInsertTask = new DbTask(getActivity());
                expenseInsertTask.execute(a.toArray(new String[a.size()]));
                Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InsertDbTask movieUpdateTask = new InsertDbTask(getActivity());
                System.out.println("Nothing saved !!");
                Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
    public class DbTask extends AsyncTask<String, Void, Void> {
        private final Context mContext;

        public DbTask(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(String... params) {
            ContentValues locationValues = new ContentValues();
            if(params[0].equals("Update")) {
                // Then add the data, along with the corresponding name of the data type,
                // so the content provider knows what kind of value is being inserted.
                locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_AMOUNT, params[2]);
                locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_CATEGORY, params[3]);
                locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_DATE, params[4]);
                locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_NOTE, params[5]);

                // Finally, insert location data into the database.
                int insertedUri = mContext.getContentResolver().update(
                        ExpenseContract.ExpenseEntry.CONTENT_URI,
                        locationValues, ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_ID+ "=?", new String[]{params[1]}
                );

                // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
//            long expenseId = ContentUris.parseId(insertedUri);
                System.out.println("Expense Saved !!" + insertedUri);
            }
            else
            {
                int deletedUri = mContext.getContentResolver().delete(
                        ExpenseContract.ExpenseEntry.CONTENT_URI,
                        ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_ID+ "=?", new String[]{params[1]}
                );
                System.out.println("Deleted!!" + deletedUri);
            }
            return null;
        }
    }
}
