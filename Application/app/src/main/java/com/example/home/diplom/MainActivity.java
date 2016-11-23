package com.example.home.diplom;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Fragment fragmentRemind;
    private Fragment fragmentNote;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private final Calendar calendar = Calendar.getInstance();
    private final Date d = new Date();
    private final int DIALOG_DATE = 0;
    private int myYear = calendar.get(Calendar.YEAR);
    private int myMonth = calendar.get(Calendar.MONTH);
    private int myDay = calendar.get(Calendar.DAY_OF_MONTH);
    private final int DIALOG_TIME = 1;
    private int myHour = d.getHours();
    private int myMinute = d.getMinutes();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFab();
        initToolbar();
        initSpinner();
        initNotification();


        manager = getSupportFragmentManager();
        fragmentRemind = new RemindFragment();
        fragmentNote = new FragmentNote();
//        Toast.makeText(this, myHour, Toast.LENGTH_SHORT).show();


    }


    private void initNotification() {

        /***
         * TODO code refactor especially initNotification
         * TODO several fab-s https://www.learn2crack.com/2015/10/android-floating-action-button-animations.html
         * TODO Navigation view burger https://youtu.be/AKSX_Ic6nLU
         */

        Context context = getApplicationContext();

        Intent intent = new Intent(context, Main2Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("бла бла бла")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("title of notification")
                .setContentText("Todo thing here");
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

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
            //TextView textView = (TextView) findViewById(R.id.textView);
            //textView.setText("Today is " + myDay + "/" + myMonth + "/" + myYear);

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
