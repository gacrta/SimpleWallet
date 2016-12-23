package com.example.carteirasimples;

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

    // possible paths to be appended on base URI
    public static final String _ID = "_id";
    public static final String VALUE = "value";
    public static final String CATEGORY = "category";
    public static final String DATE = "date";
    public static final String SIGN = "sign";

    //TODO implement entry classes

}
