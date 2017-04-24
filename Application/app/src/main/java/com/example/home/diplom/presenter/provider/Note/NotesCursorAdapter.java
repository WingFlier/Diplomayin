package com.example.home.diplom.presenter.provider.Note;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;

public class NotesCursorAdapter extends CursorAdapter
{
    public NotesCursorAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        String noteText = cursor.getString(cursor.getColumnIndex(DataBase.NOTE_TEXT));
        String noteTime = cursor.getString(cursor.getColumnIndex(DataBase.NOTE_TIME));
        /*  int pos = noteText.indexOf(10);
        if (pos != -1)
        {
            noteText = noteText.substring(0, pos) + "...";
        }*/
        TextView text = (TextView) view.findViewById(R.id.tvNote);
        TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
        text.setText(noteText);
        tvTime.setText(noteTime);
    }

    @Override
    public int getCount()
    {
        return super.getCount();
    }
}
