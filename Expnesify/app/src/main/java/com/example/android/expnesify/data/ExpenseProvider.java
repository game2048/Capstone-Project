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

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class ExpenseProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ExpenseDbHelper mOpenHelper;

    static final int EXPENSE = 100;
    static final int EXPENSE_WITH_ID = 101;
    static final int EXPENSE_FAV = 102;

    private static final SQLiteQueryBuilder sExpenseByIdQueryBuilder;

    static{
        sExpenseByIdQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sExpenseByIdQueryBuilder.setTables(
                ExpenseContract.ExpenseEntry.TABLE_NAME );
    }

    private static final String sExpenseIdSelection =
            ExpenseContract.ExpenseEntry.TABLE_NAME+
                    "." + ExpenseContract.ExpenseEntry._ID + " = ? ";


    private Cursor getWeatherByExpenseId(Uri uri, String[] projection, String sortOrder) {
        String movieId = ExpenseContract.ExpenseEntry.getExpenseIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        selectionArgs = new String[]{movieId};
        selection = sExpenseIdSelection;

        return sExpenseByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ExpenseContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, ExpenseContract.PATH_EXPENSE, EXPENSE);
        matcher.addURI(authority, ExpenseContract.PATH_EXPENSE + "/FAV", EXPENSE_FAV);
        matcher.addURI(authority, ExpenseContract.PATH_EXPENSE + "/*", EXPENSE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ExpenseDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case EXPENSE:
                return ExpenseContract.ExpenseEntry.CONTENT_ITEM_TYPE;
            case EXPENSE_WITH_ID:
                return ExpenseContract.ExpenseEntry.CONTENT_TYPE;
            case EXPENSE_FAV:
                return ExpenseContract.ExpenseEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case EXPENSE_WITH_ID:
            {
                retCursor = getWeatherByExpenseId(uri, projection, sortOrder);
                break;
            }

            case EXPENSE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ExpenseContract.ExpenseEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case EXPENSE: {
                long _id = db.insert(ExpenseContract.ExpenseEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ExpenseContract.ExpenseEntry.buildExpenseUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if ( null == selection ) selection = "1";
        switch (match) {
            case EXPENSE:
                rowsDeleted = db.delete(
                        ExpenseContract.ExpenseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case EXPENSE:
                rowsUpdated = db.update(ExpenseContract.ExpenseEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EXPENSE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ExpenseContract.ExpenseEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}