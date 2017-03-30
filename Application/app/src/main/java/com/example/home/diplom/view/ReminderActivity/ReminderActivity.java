package com.example.home.diplom.view.ReminderActivity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;
import com.example.home.diplom.presenter.provider.Reminder.ReminderCursorAdapter;
import com.example.home.diplom.presenter.provider.Reminder.ReminderProvider;
import com.example.home.diplom.view.AboutActivity.AboutActivity;
import com.example.home.diplom.view.CommonMethods;
import com.example.home.diplom.view.DrawerMenuTrueHolder;

public class ReminderActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor>
{

    public static final int ACTIVITY_REMINDER = R.layout.activity_reminder;

    private NavigationView navigationView;
    private DrawerMenuTrueHolder drawerMenuTrueHolder = new DrawerMenuTrueHolder();
    //don't delete. they are being used
    private TextView navMonth;
    private TextView navDay;
    private android.widget.CursorAdapter cursorAdapterReminder;
    ListView listViewRem;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(ACTIVITY_REMINDER);
        Toolbar toolbar = initToolbar();
        initDrawerNav(toolbar);
        View header = navigationView.getHeaderView(0);
        drawerMenuTrueHolder.setNav_remind_true(true);
        CommonMethods.getDay(header, navMonth, navDay);

//        insertNote("new Reminder", "Other", "2017,2,15");
//        insertNote("new Reminder", "BirthDay", "2000,4,25");
        //       insertNote("New Reminder", "Personal", "2017,2,15");
        //  insertNote("content");
        ReloadCursor();
        listViewRem = (ListView) findViewById(android.R.id.list);
        cursorAdapterReminder = new ReminderCursorAdapter(this, null, 0);
        listViewRem.setAdapter(cursorAdapterReminder);
        getLoaderManager().initLoader(0, null, this);

    }

    private void insertNote(String content, String category, String Time)
    {
        ContentValues values = new ContentValues();
        values.put(DataBase.REMINDER_TEXT, content);
        values.put(DataBase.REMINDER_CATEGORY, category);
        values.put(DataBase.REMINDER_ALARM_TIME, Time);
        getContentResolver().insert(ReminderProvider.CONTENT_URI, values);
    }

    private void insertNote(String content)
    {
        ContentValues values = new ContentValues();
        values.put(DataBase.REMINDER_TEXT, content);
        getContentResolver().insert(ReminderProvider.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.add_sample_notes:
                insertNote("TODO thing for doing very long text you know for what", "Other", "2017-02-15");
                insertNote("congratulate", "BirthDay", "2000-04-25");
                insertNote("Smth", "Personal", "2017-02-15");
                ReloadCursor();
                break;
            case R.id.delete_all_notes:
                //TODO maybe using delete method can delete specific note giving its id
                getContentResolver().delete(ReminderProvider.CONTENT_URI, null, null);
                ReloadCursor();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        switch (id)
        {
            case R.id.nav_notes:
                if (!drawerMenuTrueHolder.isNav_note_true())
                {
                    finish();
                }
                break;
            case R.id.nav_remind:
                if (!drawerMenuTrueHolder.isNav_remind_true())
                {
                    intent = new Intent(ReminderActivity.this, ReminderActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_about:
                intent = new Intent(ReminderActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_remind);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Toolbar initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_remind);
        toolbar.setTitle(R.string.reminders);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawerNav(Toolbar toolbar)
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_remind);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view_remind);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_remind);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    /**
     * Reminder Activity Fab click
     */
    public void onFabClick(View view)
    {
        Intent intent_rem = new Intent(ReminderActivity.this, NewReminderActivity.class);
        startActivity(intent_rem);
    }

    private void ReloadCursor()
    {
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(this, ReminderProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        cursorAdapterReminder.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        cursorAdapterReminder.swapCursor(null);
    }
}
