package com.example.home.diplom.presenter.provider.Reminder.Alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.database.Cursor;

import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;
import com.example.home.diplom.presenter.provider.Reminder.ReminderProvider;
import com.example.home.diplom.view.ReminderActivity.NewReminderActivity;
import com.example.home.diplom.view.ReminderActivity.ReminderActivity;

import java.util.Calendar;


public class AlarmReceiver extends WakefulBroadcastReceiver
{

    private final int HOURLY = 1, DAILY = 2, WEEKLY = 3, MONTHLY = 4, YEARLY = 5;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");

        DataBase database = new DataBase(context);
        Cursor cursor = database.getItem(id);
        cursor.moveToFirst();

        int frequency = cursor.getInt(cursor.getColumnIndex(DataBase.REMINDER_REPEAT_TIME));
        //String repeat = cursor.getString(cursor.getColumnIndex(DataBase.REMINDER_REPEAT_TIME));
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DataBase.REMINDER_ALARM_TIME)));
        if (frequency > 0)
        {
            if (frequency == HOURLY)
            {
                time.add(Calendar.HOUR, 1);
            } else if (frequency == DAILY)
            {
                time.add(Calendar.DATE, 1);
            } else if (frequency == WEEKLY)
            {
                time.add(Calendar.DATE, 7);
            } else if (frequency == MONTHLY)
            {
                time.add(Calendar.MONTH, 1);
            } else if (frequency == YEARLY)
            {
                time.add(Calendar.YEAR, 1);
            }
           /* database.updateTime(id, time.getTimeInMillis());
            Intent setAlarm = new Intent(context, AlarmService.class);
            setAlarm.putExtra("id", id);
            setAlarm.setAction(AlarmService.CREATE);
            context.startService(setAlarm);*/
        }

        Intent result = new Intent(context, NewReminderActivity.class);
        Uri uri = Uri.parse(ReminderProvider.CONTENT_URI + "/" + id);
        intent.putExtra(ReminderProvider.CONTENT_ITEM_TYPE, uri);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NewReminderActivity.class);
        stackBuilder.addNextIntent(result);
        PendingIntent clicked = PendingIntent.getActivity(context, 0, new Intent(context, ReminderActivity.class), 0);

        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.setBigContentTitle("New Alarm");
        bigStyle.bigText(title);
        Notification n = new NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.alarm))
                .setSmallIcon(R.mipmap.alarm)
                .setSmallIcon(R.drawable.launcher)
                .setContentText(title)
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0)
                .setStyle(bigStyle)
                .setContentIntent(clicked)
                .setAutoCancel(true)
                .build();
        SharedPreferences sharedpreferences = context.getSharedPreferences("settingPrefs", Context.MODE_PRIVATE);
        if (sharedpreferences.getInt("vibrate", -1) == 0)
        {
            n.defaults |= Notification.DEFAULT_VIBRATE;
        }
        n.defaults |= Notification.DEFAULT_VIBRATE;
        n.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        n.defaults |= Notification.DEFAULT_SOUND;

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, n);
    }
}