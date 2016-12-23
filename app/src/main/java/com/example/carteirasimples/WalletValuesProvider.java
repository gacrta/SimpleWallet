package com.example.carteirasimples;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by gabriel.almeida on 23/12/2016.
 */

public class WalletValuesProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MainDatabaseHelper mOpenHelper;

    static {
        sUriMatcher.addURI("com.example.carteirasimples", "table1", 1);
        sUriMatcher.addURI("com.example.carteirasimples", "table1/#", 2);
    }

    @Override
    public boolean onCreate() {
        //TODO implemente onCreate()
        return false;
    }

    @Override
    public String getType(Uri uri) {
        return "";
    }

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {

        switch (sUriMatcher.match(uri)){
            case 1:
                //TODO implement table
                break;
            case 2:
                //TODO implement row
                break;
            default:
                //TODO implement error handling
        }

        // TODO implement code here
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        //TODO implement insert()
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //TODO implement update()
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO implement delete()
        return 0;
    }






}
