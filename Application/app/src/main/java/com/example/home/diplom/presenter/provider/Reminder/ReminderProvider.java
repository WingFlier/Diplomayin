package com.example.home.diplom.presenter.provider.Reminder;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.home.diplom.model.DataBase;

/**
 * Created by Wingfly on 3/29/2017.
 */

public class ReminderProvider extends ContentProvider
{
    private static final String AUTHORITY =
            "com.wingfly.home.diplom.reminderprovider";
    private static final String REM_BASE_PATH = "reminder";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + REM_BASE_PATH);

    // Constant to identify the requested operation
    private static final int REMINDER = 1;
    private static final int REMINDER_ID = 2;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    //to indicate that existing note is being updated
    public static final String CONTENT_ITEM_TYPE = "REMINDER";

    /**неведомая хрень*/
    static
    {
        uriMatcher.addURI(AUTHORITY, REM_BASE_PATH, REMINDER);
        uriMatcher.addURI(AUTHORITY, REM_BASE_PATH + "/#", REMINDER_ID);
    }

    private SQLiteDatabase database;
    private DataBase base;


    @Override
    public boolean onCreate()
    {
        base = new DataBase(getContext());
        database = base.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        if (uriMatcher.match(uri) == REMINDER_ID)
        {
            selection = DataBase.REMINDER_ID + "=" + uri.getLastPathSegment();
        }
        return database.query(DataBase.TABLE_REMIND, DataBase.ALL_COLUMNS_REMINDER,
                selection, null, null, null, DataBase.REMINDER_CREATED
                        + " DESC");
        /*return database.query(DataBase.TABLE_NOTES, DataBase.ALL_COLUMNS
                , selection, null, null, null,  DataBase.NOTE_ID  + " DESC");*/
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        long id = database.insert(DataBase.TABLE_REMIND, null, values);
        return Uri.parse(REM_BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        return database.delete(DataBase.TABLE_REMIND, selection,
                selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return database.update(DataBase.TABLE_REMIND, values, selection,
                selectionArgs);
    }
}
