package com.android.dnh.moneykeeper.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.dnh.moneykeeper.data.MoneyContract.MoneyEntry;

public class MoneyDbHelper extends SQLiteOpenHelper
{
    /* Name of the database file */
    private static final String DATABASE_NAME = "moneykeeper.db";

    private static final int DATABASE_VERSION = 1;

    public MoneyDbHelper(Context context)
    {
        //null factory will use default
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String SQL_CREATE_TRANSACTION_TABLE = "CREATE TABLE " + MoneyEntry.TABLE_NAME_TRANSACTION + "("
                + MoneyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoneyEntry.COLUMN_DATE + " INTEGER NOT NULL, "
                + MoneyEntry.COLUMN_AMOUNT + " INTEGER NOT NULL, "
                + MoneyEntry.COLUMN_CURRENCY + " INTEGER NOT NULL DEFAULT 0, "
                + MoneyEntry.COLUMN_NOTE + " TEXT, "
                + MoneyEntry.COLUMN_CATEGORY + " INTEGER NOT NULL DEFAULT 0);";


        String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + MoneyEntry.TABLE_NAME_CATEGORY + "("
                + MoneyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoneyEntry.COLUMN_CATEGORY_NAME + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_TRANSACTION_TABLE);
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // for upgrading live version of the app.
        // check version then do the upgrade drop table save data etc..
    }
}
