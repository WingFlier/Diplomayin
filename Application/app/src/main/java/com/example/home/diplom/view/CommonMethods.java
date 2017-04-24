package com.example.home.diplom.view;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.TextView;

import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;

import java.util.Calendar;
import java.util.Locale;

public final class CommonMethods
{
    public static void getDay(View header, TextView navMonth, TextView navDay)
    {
        navMonth = (TextView) header.findViewById(R.id.txtMonth);
        navDay = (TextView) header.findViewById(R.id.txtDay);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        navDay.setText(String.valueOf(day));
        navMonth.setText(String.valueOf(month));
    }

    public static boolean checkIfEmpty(String table, Context context)
    {
        DataBase dataBase = new DataBase(context);
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count > 0)
            {
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public static void new_dialog(Context context)
    {
    }
}
