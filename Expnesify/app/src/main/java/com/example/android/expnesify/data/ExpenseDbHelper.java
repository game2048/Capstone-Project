package com.example.android.expnesify.data;

/**
 * Created by vaibhav.seth on 3/10/16.
 */
/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.expnesify.data.ExpenseContract.ExpenseEntry;

/**
 * Manages a local database for expense data.
 */
public class ExpenseDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "expense.db";

    public ExpenseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + ExpenseEntry.TABLE_NAME + " (" +
                ExpenseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ExpenseEntry.COLUMN_EXPENSE_AMOUNT + " REAL NOT NULL, " +
                ExpenseEntry.COLUMN_EXPENSE_CATEGORY + " TEXT NOT NULL, " +
                ExpenseEntry.COLUMN_EXPENSE_DATE + " DATE NOT NULL, " +
                ExpenseEntry.COLUMN_EXPENSE_NOTE + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
