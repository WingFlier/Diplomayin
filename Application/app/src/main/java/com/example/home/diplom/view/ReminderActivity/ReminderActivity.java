package com.example.home.diplom.view.ReminderActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.home.diplom.R;
import com.example.home.diplom.view.AboutActivity.AboutActivity;
import com.example.home.diplom.view.CommonMethods;
import com.example.home.diplom.view.DrawerMenuTrueHolder;
import com.example.home.diplom.view.NoteActivity.MainActivity;

public class ReminderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int ACTIVITY_REMINDER = R.layout.activity_reminder;

    private NavigationView navigationView;
    private DrawerMenuTrueHolder drawerMenuTrueHolder = new DrawerMenuTrueHolder();

    private TextView navMonth;
    private TextView navDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ACTIVITY_REMINDER);
        Toolbar toolbar = initToolbar();
        initDrawerNav(toolbar);
        View header = navigationView.getHeaderView(0);
        drawerMenuTrueHolder.setNav_remind_true(true);
        CommonMethods.getDay(header, navMonth, navDay);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.nav_notes:
                if (!drawerMenuTrueHolder.isNav_note_true()) {
                    finish();
                }
                break;
            case R.id.nav_remind:
                if (!drawerMenuTrueHolder.isNav_remind_true()) {
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

    public Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_remind);
        toolbar.setTitle("Напоминания");
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawerNav(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_remind);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view_remind);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_remind);
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


}
