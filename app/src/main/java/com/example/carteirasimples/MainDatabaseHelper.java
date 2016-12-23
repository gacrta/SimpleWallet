package com.example.carteirasimples;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gabriel.almeida on 23/12/2016.
 */

public class MainDatabaseHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "wallet_values.db";
    private static final String SQL_CREATE_MAIN = "CREATE_TABLE " +
            "main " +
            "(" +
            " _ID INTEGER PRIMARY KEY, " +
            " WORD TEXT FREQUENCY INTEGER " +
            " LOCATE TEXT )";

    public MainDatabaseHelper(Context context){
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MAIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
