package com.example.home.diplom.presenter.provider.Reminder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;

/**
 * Created by Wingfly on 3/29/2017.
 */

public class ReminderCursorAdapter extends CursorAdapter
{
    public ReminderCursorAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.reminder_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        String[] reminder_repeat =
                {
                        context.getString(R.string.spinner_repeat_once),
                        context.getString(R.string.spinner_repeat_1hour),
                        context.getString(R.string.spinner_repeat_1day),
                        context.getString(R.string.spinner_repeat_1week),
                        context.getString(R.string.spinner_repeat_1month),
                        context.getString(R.string.spinner_repeat_1year)
                };

        TextView txtCategory = (TextView) view.findViewById(R.id.txtCategory);
        TextView txtReminderContent = (TextView) view.findViewById(R.id.txtReminderContent);
        TextView txtReminderRepeat = (TextView) view.findViewById(R.id.textRepeat);
        TextView txtAlarmTime = (TextView) view.findViewById(R.id.txtAlarmTime);

        String reminderCategory = cursor.getString(cursor.getColumnIndex(DataBase.REMINDER_CATEGORY));
        String reminderRepeat = reminder_repeat
                [
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBase.REMINDER_REPEAT_TIME)))
                ];
        String reminderContent = cursor.getString(cursor.getColumnIndex(DataBase.REMINDER_TEXT));
        String reminderAlarmTime = cursor.getString(cursor.getColumnIndex(DataBase.REMINDER_ALARM_TIME));

        txtCategory.setText(reminderCategory);
        txtReminderContent.setText(reminderContent);
        txtAlarmTime.setText(reminderAlarmTime);
        txtReminderRepeat.setText(reminderRepeat);
    }

    @Override
    public int getCount()
    {
        return super.getCount();
    }
}
