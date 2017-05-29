package com.example.home.diplom.view.ReminderActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;
import com.example.home.diplom.presenter.provider.Reminder.ReminderCursorAdapter;
import com.example.home.diplom.presenter.provider.Reminder.ReminderProvider;
import com.example.home.diplom.view.AboutActivity.AboutActivity;
import com.example.home.diplom.view.CommonMethods;
import com.example.home.diplom.view.DrawerMenuTrueHolder;
import com.example.home.diplom.view.NoteActivity.MainActivity;
import com.example.home.diplom.view.ReminderActivity.Category.category_birthdays;
import com.example.home.diplom.view.ReminderActivity.Category.category_other;
import com.example.home.diplom.view.ReminderActivity.Category.category_personal;

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
    TextView reminder_empty;
    public static final int REMINDER_REQUEST_CODE = 101;
    private FloatingActionButton fab_reminder;
    private SharedPreferences sharedpreferences;

    public static int note_id;


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
        listViewRem = (ListView) findViewById(android.R.id.list);
        fab_reminder = (FloatingActionButton) findViewById(R.id.fab_new_reminder);
        reminder_empty = (TextView) findViewById(R.id.reminder_empty);
        check();
        cursorAdapterReminder = new ReminderCursorAdapter(this, null, 0);
        listViewRem.setAdapter(cursorAdapterReminder);
        listViewRem.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                fab_reminder.setVisibility(View.GONE);
                Intent intent = new Intent(ReminderActivity.this, NewReminderActivity.class);
                Uri uri = Uri.parse(ReminderProvider.CONTENT_URI + "/" + id);
                intent.putExtra(ReminderProvider.CONTENT_ITEM_TYPE, uri);
                note_id = (int) id;
                Log.d("logging_tag", "id for send is + " + note_id);
                Log.d("logging_tag", "uri for send is + " + uri);
                startActivityForResult(intent, REMINDER_REQUEST_CODE);
            }
        });
        listViewRem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, final long long_id)
            {
                final String querry = "_id = ?";

                final String items[] = {
                        getString(R.string.long_press_open),
                        getString(R.string.long_press_delete)
                };
                final AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int item)
                    {
                        if (items[item].equals(getString(R.string.long_press_open)))
                        {
                            Intent intent = new Intent(ReminderActivity.this, NewReminderActivity.class);
                            Uri uri = Uri.parse(ReminderProvider.CONTENT_URI + "/" + long_id);
                            intent.putExtra(ReminderProvider.CONTENT_ITEM_TYPE, uri);
                            startActivityForResult(intent, REMINDER_REQUEST_CODE);
                            dialog.dismiss();

                        } else if (items[item].equals(getString(R.string.long_press_delete)))
                        {
                            getContentResolver().delete(ReminderProvider.CONTENT_URI, querry,
                                    new String[]{String.valueOf(long_id)});
                            dialog.dismiss();
                            ReloadCursor();
                            check();
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
        getLoaderManager().initLoader(0, null, this);


    }

    private void check()
    {
        if (!CommonMethods.checkIfEmpty(DataBase.TABLE_REMIND, this))
        {
            listViewRem.setVisibility(View.GONE);
            reminder_empty.setVisibility(View.VISIBLE);
        } else
        {
            listViewRem.setVisibility(View.VISIBLE);
            reminder_empty.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume()
    {
        fab_reminder.setVisibility(View.VISIBLE);
        ReloadCursor();
        check();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.delete_all_notes:
                getContentResolver().delete(ReminderProvider.CONTENT_URI, null, null);
                ReloadCursor();
                check();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
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
                sharedpreferences = getSharedPreferences("settingPrefs", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedpreferences.edit();
                new MaterialDialog.Builder(this)
                        .title(R.string.vibrate)
                        .items(R.array.settingVibrate)
                        .cancelable(false)
                        .theme(Theme.LIGHT)
                        .itemsColor(getResources().getColor(R.color.colorAccent))
                        .itemsCallbackSingleChoice(sharedpreferences.getInt("vibrate", -1),
                                new MaterialDialog.ListCallbackSingleChoice()
                                {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text)
                                    {
                                        editor.putInt("vibrate", which).commit();
                                        Toast.makeText(ReminderActivity.this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
                                        return true;
                                    }
                                })
                        .show();
                break;
            case R.id.nav_about:
                intent = new Intent(ReminderActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_personal:
                intent = new Intent(ReminderActivity.this, category_personal.class);
                startActivity(intent);
                break;
            case R.id.nav_birthday:
                intent = new Intent(ReminderActivity.this, category_birthdays.class);
                startActivity(intent);
                break;
            case R.id.nav_other:
                intent = new Intent(ReminderActivity.this, category_other.class);
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