package com.example.home.diplom.presenter.provider.Reminder.Alarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.home.diplom.model.DataBase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmService extends IntentService
{

    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";
    public static final String DELETE = "DELETE";
    private IntentFilter matcher;

    public AlarmService()
    {
        super("AlarmService");
        matcher = new IntentFilter();
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
        matcher.addAction(DELETE);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        String action = intent.getAction();
        int id = intent.getIntExtra("id", 0);
        boolean deletedFromMain = intent.getBooleanExtra("deletedFromMain", false);

        if (matcher.matchAction(action))
        {
            execute(action, id, deletedFromMain);
        }
    }

    private void execute(String action, int id, boolean deletedFromMain)
    {

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        DataBase database = new DataBase(this);
        Cursor cursor = database.getItem(id);
        cursor.moveToFirst();

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("id", cursor.getInt(cursor.getColumnIndex(DataBase.REMINDER_ID)));
        intent.putExtra("title", cursor.getString(cursor.getColumnIndex(DataBase.REMINDER_TEXT)));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        long timeInMilliseconds = cursor.getLong(cursor.getColumnIndex(DataBase.REMINDER_ALARM_TIME_MILIS));
        if (CREATE.equals(action))
        {
            alarm.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
        } else if (DELETE.equals(action))
        {
            alarm.cancel(pendingIntent);
            database.deleteItem(id);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(id);

            if (deletedFromMain)
            {
                Intent refresh = new Intent("REFRESH");
                LocalBroadcastManager.getInstance(this).sendBroadcast(refresh);
            } else
            {
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("DELETED"));
            }
        } else if (CANCEL.equals(action))
        {
            alarm.cancel(pendingIntent);
        }
        database.close();
        cursor.close();
    }
}
