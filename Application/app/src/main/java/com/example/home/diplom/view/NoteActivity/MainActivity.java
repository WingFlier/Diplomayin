package com.example.home.diplom.view.NoteActivity;

import android.app.LoaderManager;
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
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;
import com.example.home.diplom.presenter.provider.Note.NotesCursorAdapter;
import com.example.home.diplom.presenter.provider.Note.NotesProvider;
import com.example.home.diplom.view.AboutActivity.AboutActivity;
import com.example.home.diplom.view.CommonMethods;
import com.example.home.diplom.view.DrawerMenuTrueHolder;
import com.example.home.diplom.view.ReminderActivity.Category.category_birthdays;
import com.example.home.diplom.view.ReminderActivity.Category.category_completed;
import com.example.home.diplom.view.ReminderActivity.Category.category_other;
import com.example.home.diplom.view.ReminderActivity.Category.category_personal;
import com.example.home.diplom.view.ReminderActivity.NewReminderActivity;
import com.example.home.diplom.view.ReminderActivity.ReminderActivity;

/**
 * @author Tiko :)
 */


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int ACTIVITY_MAIN = R.layout.activity_main;
    private static final int EDITOR_REQUEST_CODE = 100;

    private NavigationView navigationView;
    private TextView navMonth;
    private TextView navDay;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private DrawerMenuTrueHolder drawerMenuTrueHolder = new DrawerMenuTrueHolder();
    private android.widget.CursorAdapter cursorAdapter;
    View layout;
    SharedPreferences preferences = null;
    ListView list;
    TextView note_empty;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(ACTIVITY_MAIN);
        Toolbar toolbar = initToolbar();
        drawerMenuTrueHolder.setNav_note_true(true);
        initDrawerNav(toolbar);
        detect_run_time();
        fabAnimate();
        View header = navigationView.getHeaderView(0);
        navMonth = (TextView) header.findViewById(R.id.txtMonth);
        navDay = (TextView) header.findViewById(R.id.txtDay);
        CommonMethods.getDay(header, navMonth, navDay);
        list = (ListView) findViewById(android.R.id.list);
        note_empty = (TextView) findViewById(R.id.note_empty);
        check();


        cursorAdapter = new NotesCursorAdapter(this, null, 0);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                fab.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                Uri uri = Uri.parse(NotesProvider.CONTENT_URI + "/" + id);
                intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int item)
                    {
                        if (items[item].equals(getString(R.string.long_press_open)))
                        {
                            fab.setVisibility(View.GONE);
                            Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                            Uri uri = Uri.parse(NotesProvider.CONTENT_URI + "/" + long_id);
                            intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE, uri);
                            startActivityForResult(intent, EDITOR_REQUEST_CODE);
                            dialog.dismiss();

                        } else if (items[item].equals(getString(R.string.long_press_delete)))
                        {
                            getContentResolver().delete(NotesProvider.CONTENT_URI, querry,
                                    new String[]{String.valueOf(long_id)});
                            dialog.dismiss();
                            reloadCursor();
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
        if (!CommonMethods.checkIfEmpty(DataBase.TABLE_NOTES, this))
        {
            list.setVisibility(View.GONE);
            note_empty.setVisibility(View.VISIBLE);
        } else
        {
            list.setVisibility(View.VISIBLE);
            note_empty.setVisibility(View.GONE);
        }
    }

    private void detect_run_time()
    {
        preferences = getSharedPreferences("myprefs", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        boolean firstRun = preferences.getBoolean("first", true);
        if (firstRun)
        {
            DialogInterface.OnClickListener dialogClickListener =
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int button)
                        {
                            if (button == DialogInterface.BUTTON_POSITIVE)
                            {
                            } else if (button == DialogInterface.BUTTON_NEGATIVE)
                            {
                                finish();
                                System.exit(0);
                            } else if (button == DialogInterface.BUTTON_NEUTRAL)
                            {
                                editor.putBoolean("first", false);
                                editor.commit();
                            }
                        }
                    };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage((Html.fromHtml(getString(R.string.first_run_alert))))
                    .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                    .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                    .setNeutralButton(getString(R.string.no_more_show_first_run),
                            dialogClickListener)
                    .show();
        }

    }


    private void reloadCursor()
    {
        getLoaderManager().restartLoader(0, null, this);
    }

    private void fabAnimate()
    {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                Intent intentRem = new Intent(this, NewReminderActivity.class);
                startActivity(intentRem);
                break;
            case R.id.fab2:
                Intent intentNot = new Intent(this, NewNoteActivity.class);
                startActivityForResult(intentNot, EDITOR_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK)
        {
            reloadCursor();
        }
        fab.setVisibility(View.VISIBLE);
    }

    private void initDrawerNav(Toolbar toolbar)
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public Toolbar initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.notes);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        check();
        reloadCursor();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.delete_all_notes:
                getContentResolver().delete(NotesProvider.CONTENT_URI, null, null);
                reloadCursor();
                check();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
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
                    intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_remind:
                if (!drawerMenuTrueHolder.isNav_remind_true())
                {
                    intent = new Intent(MainActivity.this, ReminderActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_personal:
                intent = new Intent(MainActivity.this, category_personal.class);
                startActivity(intent);
                break;
            case R.id.nav_birthday:
                intent = new Intent(MainActivity.this, category_birthdays.class);
                startActivity(intent);
                break;
            case R.id.nav_other:
                intent = new Intent(MainActivity.this, category_other.class);
                startActivity(intent);
                break;
            case R.id.nav_completed:
                intent = new Intent(MainActivity.this, category_completed.class);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void animateFAB()
    {
        if (isFabOpen)
        {
            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("str", "close");
        } else
        {
            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(this, NotesProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        cursorAdapter.swapCursor(null);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        check();
        reloadCursor();
    }
}