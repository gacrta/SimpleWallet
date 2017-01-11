package com.example.carteirasimples;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gabriel.almeida on 23/12/2016.
 */

public final class WalletValuesContract {
    public static final String CONTENT_AUTHORITY =
            "com.example.carteirasimples.walletvaluesdatabase";
    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + CONTENT_AUTHORITY);

    // path to WalletValues table
    public static final String TABLE_PATH = "walletvalues";

    // table class
    public static final class WalletItens implements BaseColumns {

        // Uri with WalletValues content
        //public static final Uri CONTENT_URI =
        //        BASE_CONTENT_URI.buildUpon().appendPath(TABLE_PATH).build();
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_PATH);

        //MIME strings
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd."+CONTENT_AUTHORITY+"."+TABLE_PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd."+CONTENT_AUTHORITY+"."+TABLE_PATH;

        public static final String TABLE_NAME = "WalletValuesTable";
        public static final String COLUMN_VALUE = "Value";
        public static final String COLUMN_CATEGORY = "Category";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_SIGN = "Sign";

        public static final String[] PROJECTION_ALL =
                { _ID, COLUMN_VALUE, COLUMN_CATEGORY, COLUMN_DATE, COLUMN_SIGN};

        public static final String SORT_ORDER_DEFAULT =
                COLUMN_DATE + " DESC";

        // Define a function to build a URI to find a specific walletValue by it's identifier
        public static Uri buildWalletValueUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
