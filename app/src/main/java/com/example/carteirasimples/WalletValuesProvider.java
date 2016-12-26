package com.example.carteirasimples;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.regex.Pattern;

/**
 * Created by gabriel.almeida on 23/12/2016.
 */

public class WalletValuesProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MainDatabaseHelper mOpenHelper;
    private static final int WALLET_VALUE = 1;
    private static final int WALLET_VALUE_ID = 2;

    static {
        sUriMatcher.addURI(WalletValuesContract.CONTENT_AUTHORITY, WalletValuesContract.TABLE_PATH, WALLET_VALUE);
        sUriMatcher.addURI(WalletValuesContract.CONTENT_AUTHORITY, WalletValuesContract.TABLE_PATH.concat(Pattern.quote("/#")), WALLET_VALUE_ID);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case WALLET_VALUE:
                return WalletValuesContract.WalletItens.CONTENT_TYPE;
            case WALLET_VALUE_ID:
                return WalletValuesContract.WalletItens.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {

        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case WALLET_VALUE:
                retCursor = db.query(WalletValuesContract.WalletItens.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case WALLET_VALUE_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(WalletValuesContract.WalletItens.TABLE_NAME,
                        projection,
                        WalletValuesContract.WalletItens._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        if (sUriMatcher.match(uri) != WALLET_VALUE) {
            throw new UnsupportedOperationException("Uri " + uri + " unsupported for insertion");
        }
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id = db.insert(WalletValuesContract.WalletItens.TABLE_NAME,
                null, values);
        if (_id > 0) {
            Uri newUri = ContentUris.withAppendedId(uri,_id);
            //getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        else {
            throw new SQLException("Unable to insert row into: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows;
        switch (sUriMatcher.match(uri)) {
            case WALLET_VALUE:
                rows = db.update(WalletValuesContract.WalletItens.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows;
        switch (sUriMatcher.match(uri)) {
            case WALLET_VALUE:
                rows = db.delete(WalletValuesContract.WalletItens.TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }






}
