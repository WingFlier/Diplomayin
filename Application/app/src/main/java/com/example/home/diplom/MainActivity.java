package com.example.home.diplom;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Fragment fragmentRemind;
    private Fragment fragmentNote;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private final Calendar c = Calendar.getInstance();
    private final Date d = new Date();
    private final int DIALOG_DATE = 0;
    private int myYear = c.get(Calendar.YEAR);
    private int myMonth = c.get(Calendar.MONTH);
    private int myDay = c.get(Calendar.DAY_OF_MONTH);
    private final int DIALOG_TIME = 1;
    private int myHour = d.getHours();
    private int myMinute = d.getMinutes();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFab();
        initToolbar();
        initSpinner();

        manager = getSupportFragmentManager();
        fragmentRemind = new RemindFragment();
        fragmentNote = new FragmentNote();

    }

    private void initSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {


                if (selectedId == 0) {

                    transaction = manager.beginTransaction();
                    transaction.add(R.id.layout1, fragmentNote);
                    transaction.remove(fragmentRemind);

                } else if (selectedId == 1) {

                    transaction = manager.beginTransaction();
                    transaction.add(R.id.layout, fragmentRemind);
                    transaction.remove(fragmentNote);
                }
                transaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this, "Choose something", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (fragmentRemind.isAdded()) {
                    showDialog(DIALOG_DATE);

                } else if (fragmentNote.isAdded()) {
                    Toast.makeText(MainActivity.this, "fragment note", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog Dpd = new DatePickerDialog(this, myCallBack, myYear, myMonth, myDay);
            return Dpd;
        } else if (id == DIALOG_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, myTimeBack, myHour, myMinute, true);
            return tpd;
        }
        return null;
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear + 1;
            myDay = dayOfMonth;
            //TODO: jamy u date-y Stringov stanal
            //tvDate.setText("Today is " + myDay + "/" + myMonth + "/" + myYear);
            showDialog(DIALOG_TIME);
        }
    };
    TimePickerDialog.OnTimeSetListener myTimeBack = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myHour = hourOfDay;
            myMinute = minute;
            // tvDate.setText("Time is " + myHour + " hours " + myMinute + " minutes");
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
