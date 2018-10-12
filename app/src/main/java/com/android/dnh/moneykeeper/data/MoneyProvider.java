package com.android.dnh.moneykeeper.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.dnh.moneykeeper.data.MoneyContract.MoneyEntry;

/**
 * Content Provider for MoneyDb
 */

public class MoneyProvider extends ContentProvider
{
    private static final String LOG_TAG = "MoneyProvider";
    private MoneyDbHelper moneyDbHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int TRANSACTION = 100;
    private static final int TRANSACTION_ID = 101;
    private static final int CATEGORY = 200;
    private static final int CATEGORY_ID = 201;

    static {
        sUriMatcher.addURI(MoneyContract.CONTENT_AUTHORITY, MoneyEntry.TABLE_NAME_TRANSACTION,TRANSACTION);
        sUriMatcher.addURI(MoneyContract.CONTENT_AUTHORITY, MoneyEntry.TABLE_NAME_TRANSACTION + "/#",TRANSACTION_ID);
        sUriMatcher.addURI(MoneyContract.CONTENT_AUTHORITY, MoneyEntry.TABLE_NAME_CATEGORY,CATEGORY);
        sUriMatcher.addURI(MoneyContract.CONTENT_AUTHORITY, MoneyEntry.TABLE_NAME_CATEGORY + "/#",CATEGORY_ID);
    }

    @Override
    public boolean onCreate()
    {
        // Create and initialize DB helper Object
        moneyDbHelper = new MoneyDbHelper(getContext());
        return true;
    }

    /**
     * Query method for getting data from Content Provider using URI matcher
     * @param uri The URI to query
     * @param projection The list of columns to put into the cursor (null for all column)
     * @param selection A selection criteria to apply when filtering rows.
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        SQLiteDatabase database = moneyDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri); // Getting an int that match Uri setup in initialization
        switch(match){
            case TRANSACTION:
                cursor = database.query(MoneyEntry.TABLE_NAME_TRANSACTION,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case TRANSACTION_ID:
                selection = MoneyEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MoneyEntry.TABLE_NAME_TRANSACTION,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY:
                cursor = database.query(MoneyEntry.TABLE_NAME_CATEGORY,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY_ID:
                selection = MoneyEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MoneyEntry.TABLE_NAME_CATEGORY,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw  new IllegalArgumentException("Cannot query Unknown URI: " + uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        int match = sUriMatcher.match(uri); // Getting an int that match Uri setup in initialization
        switch(match){
            case TRANSACTION:
                return MoneyEntry.CONTENT_TRANSACTION_LIST_TYPE;
            case TRANSACTION_ID:
                return MoneyEntry.CONTENT_TRANSACTION_ITEM_TYPE;
            case CATEGORY:
                return MoneyEntry.CONTENT_CATEGORY_LIST_TYPE;
            case CATEGORY_ID:
                return MoneyEntry.CONTENT_CATEGORY_ITEM_TYPE;
            default:
                throw  new IllegalArgumentException("Cannot query Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        int match = sUriMatcher.match(uri); // Getting an int that match Uri setup in initialization
        switch (match){
            case TRANSACTION:
                return insertTransaction(uri, values);
            case CATEGORY:
                return insertCatalog(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not support for: " + uri);
        }
    }

    // TODO: add validation checks
    private Uri insertTransaction(Uri uri, ContentValues values){
        SQLiteDatabase database = moneyDbHelper.getWritableDatabase();
        long id = database.insert(MoneyEntry.TABLE_NAME_TRANSACTION,null,values);
        if(id == -1){
            Log.e(LOG_TAG, "Fail to inserted row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri,id);
    }

    // TODO: add validation checks
    private Uri insertCatalog(Uri uri, ContentValues values){
        SQLiteDatabase database = moneyDbHelper.getWritableDatabase();
        long id = database.insert(MoneyEntry.TABLE_NAME_CATEGORY,null,values);
        if(id == -1){
            Log.e(LOG_TAG, "Fail to inserted row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        int match = sUriMatcher.match(uri); // Getting an int that match Uri setup in initialization
        switch(match){
            case TRANSACTION:
                return updateTransaction(values,selection,selectionArgs);
            case TRANSACTION_ID:
                selection = MoneyEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateTransaction(values,selection,selectionArgs);
            case CATEGORY:
                return updateCategory(values,selection,selectionArgs);
            case CATEGORY_ID:
                selection = MoneyEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateCategory(values,selection,selectionArgs);
            default:
                throw  new IllegalArgumentException("Cannot query Unknown URI: " + uri);
        }
    }

    // TODO: add validation checks
    private int updateTransaction(ContentValues values, String selection, String[] selectionArgs){
        SQLiteDatabase database = moneyDbHelper.getWritableDatabase();
        return database.update(MoneyEntry.TABLE_NAME_TRANSACTION,values,selection,selectionArgs);
    }

    // TODO: add validation checks
    private int updateCategory(ContentValues values, String selection, String[] selectionArgs){
        SQLiteDatabase database = moneyDbHelper.getWritableDatabase();
        return database.update(MoneyEntry.TABLE_NAME_CATEGORY,values,selection,selectionArgs);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        SQLiteDatabase database = moneyDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri); // Getting an int that match Uri setup in initialization
        switch(match){
            case TRANSACTION:
                return database.delete(MoneyEntry.TABLE_NAME_TRANSACTION,selection,selectionArgs);
            case TRANSACTION_ID:
                selection = MoneyEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return database.delete(MoneyEntry.TABLE_NAME_TRANSACTION,selection,selectionArgs);
            case CATEGORY:
                return database.delete(MoneyEntry.TABLE_NAME_CATEGORY,selection,selectionArgs);
            case CATEGORY_ID:
                selection = MoneyEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return database.delete(MoneyEntry.TABLE_NAME_CATEGORY,selection,selectionArgs);
            default:
                throw  new IllegalArgumentException("Cannot query Unknown URI: " + uri);
        }
    }
}
