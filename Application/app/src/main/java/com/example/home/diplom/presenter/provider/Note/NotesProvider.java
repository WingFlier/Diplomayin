package com.example.home.diplom.presenter.provider.Note;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.home.diplom.model.DataBase;

public class NotesProvider extends ContentProvider
{

    private static final String AUTHORITY =
            "com.wingfly.home.diplom.notesprovider";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    //to indicate that existing note is being updated
    public static final String CONTENT_ITEM_TYPE = "Note";

    static
    {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID);
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
        if (uriMatcher.match(uri) == NOTES_ID)
        {
            Log.d("str", "selection is: " + selection);
            Log.d("str", "Note.Id is: " + DataBase.NOTE_ID + "=" + uri.getLastPathSegment());
            Log.d("str", "uri.getLastPathSegment(); is: " + uri.getLastPathSegment());
            selection = DataBase.NOTE_ID + "=" + uri.getLastPathSegment();
        }
        return database.query(DataBase.TABLE_NOTES, DataBase.ALL_COLUMNS_NOTE
                , selection, null, null, null, DataBase.NOTE_ID
                        + " DESC");
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
        Log.d("str", "Insert function called");
        long id = database.insert(DataBase.TABLE_NOTES, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        Log.d("str", "Delete function called");
        return database.delete(DataBase.TABLE_NOTES, selection,
                selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        Log.d("str", "Update function called");
        return database.update(DataBase.TABLE_NOTES, values, selection,
                selectionArgs);
    }
}
