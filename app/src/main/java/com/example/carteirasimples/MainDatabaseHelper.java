package com.example.carteirasimples;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by gabriel.almeida on 23/12/2016.
 */

public class MainDatabaseHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "wallet_values.db";
    private static final int DB_VERSION = 12;
    private static final String SQL_CREATE = "CREATE TABLE " +
            WalletValuesContract.WalletItens.TABLE_NAME +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WalletValuesContract.WalletItens.COLUMN_VALUE + " TEXT, " +
            WalletValuesContract.WalletItens.COLUMN_CATEGORY + " TEXT, " +
            WalletValuesContract.WalletItens.COLUMN_DATE + " INTEGER, " +
            WalletValuesContract.WalletItens.COLUMN_SIGN + " TEXT);";

    public MainDatabaseHelper(Context context){
        super(context, DBNAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion < 2) {
            db.beginTransaction();
            db.execSQL("CREATE TABLE IF NOT EXISTS " +
                    WalletValuesContract.WalletItens.TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    WalletValuesContract.WalletItens.COLUMN_VALUE + " TEXT, " +
                    WalletValuesContract.WalletItens.COLUMN_CATEGORY + " TEXT, " +
                    WalletValuesContract.WalletItens.COLUMN_DATE + " INTEGER, " +
                    WalletValuesContract.WalletItens.COLUMN_SIGN + " TEXT);");

            db.execSQL("ALTER TABLE " +
                    WalletValuesContract.WalletItens.TABLE_NAME +
            " RENAME TO TEMP;");

            db.execSQL(SQL_CREATE);

            db.execSQL("INSERT INTO " +
                    WalletValuesContract.WalletItens.TABLE_NAME + " ("+
                    WalletValuesContract.WalletItens.COLUMN_VALUE + "," +
                    WalletValuesContract.WalletItens.COLUMN_CATEGORY + "," +
                    WalletValuesContract.WalletItens.COLUMN_SIGN + ") SELECT " +
                    WalletValuesContract.WalletItens.COLUMN_VALUE + "," +
                    WalletValuesContract.WalletItens.COLUMN_CATEGORY + "," +
                    WalletValuesContract.WalletItens.COLUMN_SIGN + " FROM TEMP;"
            );

            Cursor cursor;
            try {
                cursor = db.rawQuery("SELECT " +
                        WalletValuesContract.WalletItens.COLUMN_DATE +
                " FROM TEMP;", null);

                String currDate;
                String[] splitedDate;
                int day, month, year;
                Calendar c = Calendar.getInstance(Locale.getDefault());
                long l;

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    currDate = cursor.getString(
                            cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_DATE));

                    splitedDate = currDate.split(Pattern.quote("/"));
                    year = Integer.parseInt(splitedDate[0]);
                    month = Integer.parseInt(splitedDate[1]);
                    day = Integer.parseInt(splitedDate[2]);

                    c.set(year,month-1,day);
                    l = c.getTimeInMillis();

                    ContentValues cv = new ContentValues();
                    cv.put(WalletValuesContract.WalletItens.COLUMN_DATE, l);
                    db.update(WalletValuesContract.WalletItens.TABLE_NAME,
                            cv, WalletValuesContract.WalletItens._ID + " = ?",
                            new String[]{Integer.toString(cursor.getInt(cursor.getColumnIndex(WalletValuesContract.WalletItens._ID)))});
                    cursor.moveToNext();
                }
                cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            db.execSQL("DROP TABLE TEMP;");
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        if (oldVersion < DB_VERSION) {
            db.beginTransaction();

            Cursor cursor = db.rawQuery(String.format("select %s, %s from %s where %s  IS NULL OR  %s = ''",
                    WalletValuesContract.WalletItens.COLUMN_DATE,
                    WalletValuesContract.WalletItens._ID,
                    WalletValuesContract.WalletItens.TABLE_NAME,
                    WalletValuesContract.WalletItens.COLUMN_DATE,
                    WalletValuesContract.WalletItens.COLUMN_DATE), null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                long l = cursor.getLong(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_DATE));
                ContentValues contentValues = new ContentValues();
                contentValues.put(WalletValuesContract.WalletItens.COLUMN_DATE, System.currentTimeMillis());
                int i = db.update(WalletValuesContract.WalletItens.TABLE_NAME, contentValues,
                        WalletValuesContract.WalletItens._ID + " = ?",
                        new String[]{Integer.toString(cursor.getInt(cursor.getColumnIndex(WalletValuesContract.WalletItens._ID)))});
                cursor.moveToNext();
            }
            cursor.close();
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }
}
