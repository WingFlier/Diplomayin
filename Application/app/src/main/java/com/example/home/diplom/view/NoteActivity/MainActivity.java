package com.example.home.diplom.view.NoteActivity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;
import com.example.home.diplom.other.Main2Activity;
import com.example.home.diplom.presenter.provider.NotesCursorAdapter;
import com.example.home.diplom.presenter.provider.NotesProvider;
import com.example.home.diplom.view.AboutActivity.AboutActivity;
import com.example.home.diplom.view.CommonMethods;
import com.example.home.diplom.view.DrawerMenuTrueHolder;
import com.example.home.diplom.view.ReminderActivity.ReminderActivity;

import java.util.Calendar;
import java.util.Locale;

/**
 * @author Tiko :)
 */


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    public static final int ACTIVITY_MAIN = R.layout.activity_main;
    private static final int EDITOR_REQUEST_CODE = 100;
    private static final String PREFS_NAME = "myprefs";


    private String m_Text = "";
    private NavigationView navigationView;
    private TextView navHead;
    private TextView navMonth;
    private TextView navDay;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    DrawerMenuTrueHolder drawerMenuTrueHolder = new DrawerMenuTrueHolder();
    boolean defValue;
    //adapter for listView;
    private android.widget.CursorAdapter cursorAdapter;
    private RelativeLayout.LayoutParams lp;
    SharedPreferences.Editor editor;
    SharedPreferences settings;




    @Override
    protected void onDestroy() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ACTIVITY_MAIN);
        Toolbar toolbar = initToolbar();
        drawerMenuTrueHolder.setNav_note_true(true);
        initDrawerNav(toolbar);
        fabAnimate();
        View header = navigationView.getHeaderView(0);
        navMonth = (TextView) header.findViewById(R.id.txtMonth);
        navDay = (TextView) header.findViewById(R.id.txtDay);
        CommonMethods.getDay(header, navMonth, navDay);
        ListView list = (ListView) findViewById(android.R.id.list);




        //insertNote("new \n hi ");
        //for base update when delete or add
        //ReloadCursor();

        View layout = findViewById(R.id.main_Layout);
        cursorAdapter = new NotesCursorAdapter(this, null, 0);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                Uri uri = Uri.parse(NotesProvider.CONTENT_URI + "/" + id);
                intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });


        getLoaderManager().initLoader(0, null, this);
        //TODO not working
        if (cursorAdapter == null) {
            TextView textCenter = new TextView(this);
            textCenter.setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));


            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textCenter.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            textCenter.setLayoutParams(lp);

            textCenter.setGravity(Gravity.BOTTOM);

            textCenter.setText("Create Your First Note By Clicking The Plus Button");
            textCenter.setWidth(600);
            textCenter.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textCenter.setTextSize(35);
            textCenter.setTextColor(getResources().getColor(R.color.Grey));
            ((RelativeLayout) layout).addView(textCenter);
        }


    }




    private void ReloadCursor() {
        getLoaderManager().restartLoader(0, null, this);

    }

    private void insertNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DataBase.NOTE_TEXT, note);
        Uri noteURi = getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        Log.d("stringstring", "inserted note " + noteURi.getLastPathSegment());

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
        toolbar.setTitle("Заметки");
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
            case R.id.delete_all_notes:
                DeleteAllNotes();
        }

        return super.onOptionsItemSelected(item);
    }



    private void DeleteAllNotes() {
        getContentResolver().delete(NotesProvider.CONTENT_URI, null, null);
        ReloadCursor();
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
