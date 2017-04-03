package com.example.home.diplom.view.ReminderActivity.Category;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;
import com.example.home.diplom.presenter.provider.Reminder.ReminderCursorAdapter;

public class category_birthdays extends AppCompatActivity
{

    private android.widget.CursorAdapter cursorAdapterReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        String cat = getString(R.string.spinner_birthDays);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        cursorAdapterReminder = new ReminderCursorAdapter(this, null, 0);
        DataBase dataBaseHandler = new DataBase(this);
        SQLiteDatabase db = dataBaseHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM reminder WHERE reminderCategory = ?",
                new String[]{cat});
        ListView listView = (ListView) findViewById(R.id.listV);
        listView.setAdapter(cursorAdapterReminder);
        cursorAdapterReminder.changeCursor(cursor);
    }


}
