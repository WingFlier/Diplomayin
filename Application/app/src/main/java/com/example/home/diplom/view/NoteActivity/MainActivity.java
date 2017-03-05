package com.example.home.diplom.view.NoteActivity;

import android.app.LoaderManager;
import android.content.ContentValues;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;
import com.example.home.diplom.presenter.provider.NotesCursorAdapter;
import com.example.home.diplom.presenter.provider.NotesProvider;
import com.example.home.diplom.view.AboutActivity.AboutActivity;
import com.example.home.diplom.view.CommonMethods;
import com.example.home.diplom.view.DrawerMenuTrueHolder;
import com.example.home.diplom.view.ReminderActivity.ReminderActivity;

/**
 * @author Tiko :)
 */


//TODO new alertdialog with text create notes by clicking plus button..
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    public static final int ACTIVITY_MAIN = R.layout.activity_main;
    private static final int EDITOR_REQUEST_CODE = 100;


    private NavigationView navigationView;
    private TextView navMonth;
    private TextView navDay;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    DrawerMenuTrueHolder drawerMenuTrueHolder = new DrawerMenuTrueHolder();
    //adapter for listView;
    private android.widget.CursorAdapter cursorAdapter;
    View layout;
    SharedPreferences preferences = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        ListView list = (ListView) findViewById(android.R.id.list);


        //insertNote("new \n hi ");
        //for base update when delete or add
        //ReloadCursor();

        layout = findViewById(R.id.main_Layout);
        cursorAdapter = new NotesCursorAdapter(this, null, 0);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fab.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                Uri uri = Uri.parse(NotesProvider.CONTENT_URI + "/" + id);
                intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
                Log.d("notess", "on click position " + String.valueOf(position) + " id " + String.valueOf(id));
            }
        });
        getLoaderManager().initLoader(0, null, this);

    }

    private void detect_run_time() {

        preferences = getSharedPreferences("myprefs", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        boolean firstRun = preferences.getBoolean("first", true);
        if (firstRun) {

            DialogInterface.OnClickListener dialogClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int button) {
                            if (button == DialogInterface.BUTTON_POSITIVE) {
                            } else if (button == DialogInterface.BUTTON_NEGATIVE) {
                                finish();
                                System.exit(0);
                            } else if (button == DialogInterface.BUTTON_NEUTRAL) {
                                editor.putBoolean("first", false);
                                editor.commit();
                            }
                        }
                    };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage((Html.fromHtml(getString(R.string.first_run_alert))))
                    .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                    .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                    .setNeutralButton(getString(R.string.no_more_show_first_run), dialogClickListener)
                    .show();
        }

    }


    private void ReloadCursor() {
        getLoaderManager().restartLoader(0, null, this);



    }

    private void fabAnimate() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:

                Log.d("str", "fab1");
                break;
            case R.id.fab2:
                Intent intent = new Intent(this, NewNoteActivity.class);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
                break;

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == RESULT_OK) {
            ReloadCursor();
        }
        fab.setVisibility(View.VISIBLE);
    }

    private void initDrawerNav(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.notes);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
           /* case R.id.add_sample_notes:
                insertNote("sample note1");
                insertNote("sample \nnote2");
                insertNote("sample note3 which is longer than the others ");
                ReloadCursor();
                break;*/
            case R.id.delete_all_notes:
                getContentResolver().delete(NotesProvider.CONTENT_URI, null, null);
                ReloadCursor();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void insertNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DataBase.NOTE_TEXT, note);
        Uri noteURi = getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        Log.d("stringstring", "inserted note " + noteURi.getLastPathSegment());

    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.nav_notes:
                if (!drawerMenuTrueHolder.isNav_note_true()) {
                    intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_remind:
                if (!drawerMenuTrueHolder.isNav_remind_true()) {
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void animateFAB() {
        if (isFabOpen) {
            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("str", "close");
        } else {
            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("str", "open");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, NotesProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }


}