package com.example.android.expnesify;

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

        final Button cancelBtn = (Button) root.findViewById(
                R.id.cancel);
        final Button saveBtn = (Button) root.findViewById(
                R.id.save
        );
        final Button deleteBtn = (Button) root.findViewById(
                R.id.delete
        );
    final TextView amount = (TextView) root.findViewById(
                R.id.amountText);
        final TextView note = (TextView) root.findViewById(
                R.id.note1);
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
                if (amount.getText().toString().trim().equals("")) {

                    amount.setError("Amount is required!");
                    Toast.makeText(getActivity(), "Enter Amount", Toast.LENGTH_SHORT).show();
                } else if (fromDateEtxt.getText().toString().trim().equals("")) {
                    fromDateEtxt.setError("Date is required!");
                    Toast.makeText(getActivity(), "Enter Date", Toast.LENGTH_SHORT).show();
                } else if (category.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), "Select a expense category", Toast.LENGTH_SHORT).show();
                } else {
                    a.add("Update");
                    a.add(in.getStringExtra("id"));
                    a.add(amount.getText().toString());
                    a.add(((RadioButton) root.findViewById(category.getCheckedRadioButtonId())).getText().toString());
//                a.add(date.getText().toString());
                    a.add(fromDateEtxt.getText().toString());
                    a.add(note.getText().toString());

                    DbTask expenseInsertTask = new DbTask(getActivity());
                    expenseInsertTask.execute(a.toArray(new String[a.size()]));
                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
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
                Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
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
                locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_AMOUNT, params[2]);
                locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_CATEGORY, params[3]);
                locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_DATE, params[4]);
                locationValues.put(ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_NOTE, params[5]);

                int insertedUri = mContext.getContentResolver().update(
                        ExpenseContract.ExpenseEntry.CONTENT_URI,
                        locationValues, ExpenseContract.ExpenseEntry.COLUMN_EXPENSE_ID+ "=?", new String[]{params[1]}
                );

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
