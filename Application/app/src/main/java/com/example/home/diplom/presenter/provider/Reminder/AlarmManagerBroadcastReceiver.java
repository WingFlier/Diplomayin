package com.example.home.diplom.presenter.provider.Reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.example.home.diplom.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Wingfly on 4/9/2017.
 */

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver
{
    final public static String ONE_TIME = "onetime";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myTag");
        wl.acquire();

        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mp = MediaPlayer.create(context, notification);


        if (extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE))
        {
            msgStr.append("One time timer: ");
        }
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msgStr.append(formatter.format(new Date()));
        Toast.makeText(context, msgStr, Toast.LENGTH_SHORT).show();
        v.vibrate(2000);
        mp.start();
        wl.release();
    }

    public void setAlarm(Context context, long time, Object repeat)
    {
        String[] reminder_repeat =
                {
                        context.getString(R.string.spinner_repeat_once),
                        context.getString(R.string.spinner_repeat_15min),
                        context.getString(R.string.spinner_repeat_1day),
                        context.getString(R.string.spinner_repeat_1week),
                        context.getString(R.string.spinner_repeat_1year)
                };

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        if (repeat == reminder_repeat[0])
        {
            intent.putExtra(ONE_TIME, Boolean.TRUE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
            am.set(AlarmManager.RTC_WAKEUP, time, pi);
        } else
        {
            long interval;
            intent.putExtra(ONE_TIME, Boolean.FALSE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
            for (int i = 1; i < reminder_repeat.length; i++)
            {
                if (repeat == reminder_repeat[i])
                {
                    Log.d("strolol", reminder_repeat[i]);
                }
            }
            am.setRepeating(AlarmManager.RTC_WAKEUP, time, 5 * 1000, pi);
        }
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }
}