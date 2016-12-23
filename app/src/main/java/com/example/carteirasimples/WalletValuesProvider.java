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
    private static final int WALLET_VALUE = 1;
    private static final int WALLET_VALUE_ID = 2;

    static {
        sUriMatcher.addURI(WalletValuesContract.CONTENT_AUTHORITY, WalletValuesContract.WalletItens.TABLE_NAME, WALLET_VALUE);
        sUriMatcher.addURI("com.example.carteirasimples", WalletValuesContract.WalletItens.TABLE_NAME.concat("/#"), WALLET_VALUE_ID);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MainDatabaseHelper(getContext());
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
