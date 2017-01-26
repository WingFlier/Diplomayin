package com.example.home.diplom.view;


import android.view.View;
import android.widget.TextView;

import com.example.home.diplom.R;

import java.util.Calendar;
import java.util.Locale;

public  final class CommonMethods   {
    public static void  getDay(View header, TextView navMonth, TextView navDay){
        navMonth = (TextView) header.findViewById(R.id.txtMonth);
        navDay = (TextView) header.findViewById(R.id.txtDay);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        navDay.setText(String.valueOf(day));
        navMonth.setText(String.valueOf(month));
    }



}
