package com.example.home.diplom.presenter.provider.Reminder.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.home.diplom.model.DataBase;

import java.util.Calendar;

public class AlarmSetter extends BroadcastReceiver
{

    @Override
    // once phone reboot complete, set back all alarms
    public void onReceive(Context context, Intent intent)
    {
        DataBase database = new DataBase(context);
        Cursor cursor = database.getAllItems();
        try
        {
            while (cursor.moveToNext())
            {
                long time = cursor.getLong(cursor.getColumnIndex(DataBase.REMINDER_ALARM_TIME));
                Intent service = new Intent(context, AlarmService.class);
                service.setAction(AlarmService.CREATE);
                service.putExtra("id",
                        cursor.getInt(cursor.getColumnIndex(DataBase.REMINDER_ID)));
                context.startService(service);
            }
        } finally
        {
            cursor.close();
        }

    }

}

