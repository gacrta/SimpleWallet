package com.example.carteirasimples;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gabriel.almeida on 23/12/2016.
 */

public class MainDatabaseHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "wallet_values.db";
    private static final String SQL_CREATE = "CREATE TABLE " +
            WalletValuesContract.WalletItens.TABLE_NAME +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WalletValuesContract.WalletItens.COLUMN_VALUE + " TEXT NOT NULL, " +
            WalletValuesContract.WalletItens.COLUMN_CATEGORY + " TEXT NOT NULL, " +
            WalletValuesContract.WalletItens.COLUMN_DATE + " TEXT NOT NULL, " +
            WalletValuesContract.WalletItens.COLUMN_SIGN + " TEXT NOT NULL);";

    public MainDatabaseHelper(Context context){
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
