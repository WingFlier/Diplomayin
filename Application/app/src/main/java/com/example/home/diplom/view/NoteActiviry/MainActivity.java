package com.example.home.diplom.view.NoteActiviry;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;
import com.example.home.diplom.other.Main2Activity;
import com.example.home.diplom.presenter.provider.NotesProvider;
import com.example.home.diplom.view.DrawerMenuTrueHolder;

import java.util.Calendar;
import java.util.Locale;

/**
 * @author Tiko :)
 */


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    public static final int ACTIVITY_MAIN = R.layout.activity_main;


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
    CursorAdapter cursorAdapter;


    //TODO modify CommmonMethods class, move there commont methods of reminder, note,  about (especcially drawer , fab for mainactivity and reminderactivity);

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
        navHead = (TextView) header.findViewById(R.id.nav_header_main_textView);
        navMonth = (TextView) header.findViewById(R.id.txtMonth);
        navDay = (TextView) header.findViewById(R.id.txtDay);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        navDay.setText(String.valueOf(day));
        navMonth.setText(String.valueOf(month));


        /**
         * TODO fix firstrun feature;
         * TODO move saving feature to model
         SharedPreferences sharedPref;
         SharedPreferences.Editor editor;
         boolean first_run = true;
         sharedPref = getSharedPreferences("mypref", 0);
         editor = sharedPref.edit();
         if (first_run) {
         Toast.makeText(this, "первый запуск", Toast.LENGTH_SHORT).show();
         initAlertDialog();
         }
         first_run = true;
         editor.putBoolean("num", first_run);
         first_run = sharedPref.getBoolean("num", defValue);
         Log.d("str", m_Text);
         editor.commit();
         */

       /* TextView text = new TextView(this);
        text.setText("Create Your First Note");
        text.setWidth(230);
        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(30);
        text.setTextColor(getResources().getColor(R.color.Grey));*/


        /** insertNote("new Note");*/
        //for base update when delete or add
        //getLoaderManager().restartLoader(0 , null , this);

        String[] from = {DataBase.NOTE_TEXT};
        int[] to = {android.R.id.text1};
        cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null, from, to, 0);
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);
        getLoaderManager().initLoader(0, null, this);


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
                Log.d("str", "fab2");
                break;

        }
    }

    private void initAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your name");


        final EditText input = new EditText(this);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                m_Text = input.getText().toString();
                //  Toast.makeText(MainActivity.this, m_Text, Toast.LENGTH_SHORT).show();
                navHead.setText(m_Text);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
                System.exit(0);
            }
        });

        builder.show();

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_about:
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

    //background processed as asynctask


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
