package com.example.home.diplom.model;


import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper
{


    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 3;


    /*************NOTES***************/
    /***********
     * Tables
     **************/
    public static final String TABLE_NOTES = "notes";
    /***********
     * Columns
     **************/
    public static final String NOTE_ID = "_id";
    public static final String NOTE_TEXT = "noteText";
    public static final String NOTE_CREATED = "noteCreated";


    /********
     * NOTES
     ********/
    private static final String TABLE_NOTE_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOTE_TEXT + " TEXT, " +
                    NOTE_CREATED + " DATETIME  DEFAULT CURRENT_TIMESTAMP)";

    /*************Reminder*****************/

    /***********
     * Tables
     **************/
    public static final String TABLE_REMIND = "reminder";

    /***********
     * Columns
     **************/
    public static final String REMINDER_ID = "_id";
    public static final String REMINDER_TEXT = "reminderContent";
    public static final String REMINDER_CATEGORY = "reminderCategory";
    public static final String REMINDER_ALARM_TIME = "reminderAlarmTime";
    public static final String REMINDER_CREATED = "reminderCreated";

    /**********
     * Reminder
     *********/
    public static final String TABLE_REMIND_CREATE =
            "CREATE TABLE " + TABLE_REMIND + " ("
                    + REMINDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + REMINDER_TEXT + " TEXT, "
                    + REMINDER_CATEGORY + " TEXT, "
                    + REMINDER_ALARM_TIME + " TEXT, "
                    + REMINDER_CREATED + " TEXT default CURRENT_TIMESTAMP)";


    /**
     * REMINDER
     */
    public static final String[] ALL_COLUMNS_REMINDER = {
            REMINDER_ID, REMINDER_TEXT, REMINDER_CATEGORY,
            REMINDER_ALARM_TIME, REMINDER_CREATED};
    /********
     * NOTES
     ********/
    public static final String[] ALL_COLUMNS_NOTE = {NOTE_ID, NOTE_TEXT, NOTE_CREATED};

    public DataBase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Notes
        db.execSQL(TABLE_NOTE_CREATE);
        //Reminder
        db.execSQL(TABLE_REMIND_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Note
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        //Reminder
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMIND);
        onCreate(db);
    }




}
