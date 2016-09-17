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

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Defines table and column names for the weather database.
 */
public class ExpenseContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.android.expnesify";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EXPENSE = "expense";

    public static final class ExpenseEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXPENSE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPENSE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPENSE;

        // Table name
        public static final String TABLE_NAME = "expense";

        public static final String COLUMN_EXPENSE_ID = "_id";

        public static final String COLUMN_EXPENSE_DATE = "expense_date";
        public static final String COLUMN_EXPENSE_NOTE = "expense_note";
        public static final String COLUMN_EXPENSE_CATEGORY = "expense_category";
        public static final String COLUMN_EXPENSE_AMOUNT = "expense_amount";


        public static Uri buildExpenseUri(Long id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        public static Uri buildExpenseUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }


        public static Uri buildExpenseUri() {
            return CONTENT_URI;
        }

        public static String getExpenseIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
