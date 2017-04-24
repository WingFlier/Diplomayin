package com.example.home.diplom.view.ReminderActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;
import com.example.home.diplom.presenter.provider.Reminder.Alarm.AlarmService;
import com.example.home.diplom.presenter.provider.Reminder.ReminderProvider;

import java.util.Calendar;

public class NewReminderActivity extends AppCompatActivity
{
    DataBase dataBase;
    // time in milis holder
    long milissDate;

    TextView buttonResult;
    TextView buttonDate;
    TextView buttonTime;
    Spinner spinner_category;
    Spinner spinner_repeat;
    Calendar dateAndTime = Calendar.getInstance();
    EditText txtNewRem;

    private String action;
    private String reminderFilter;
    private String oldText;
    private String oldDateTime;
    private String oldSpinnerCategory;
    private String oldSpinnerRepeat;
    String[] repeat;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dataBase = new DataBase(this);

        String[] categories_compare =
                {
                        getString(R.string.spinner_personal),
                        getString(R.string.spinner_birthDays),
                        getString(R.string.spinner_other)
                };
        String[] reminder_repeat =
                {
                        getString(R.string.spinner_repeat_once),
                        getString(R.string.spinner_repeat_1hour),
                        getString(R.string.spinner_repeat_1day),
                        getString(R.string.spinner_repeat_1week),
                        getString(R.string.spinner_repeat_1month),
                        getString(R.string.spinner_repeat_1year)
                };
        repeat = reminder_repeat;

        spinner_category = (Spinner) findViewById(R.id.spinner_category);
        spinner_repeat = (Spinner) findViewById(R.id.spinner_repeat);
        txtNewRem = (EditText) findViewById(R.id.editText2);
        buttonDate = (TextView) findViewById(R.id.buttonDate);
        buttonTime = (TextView) findViewById(R.id.buttonTime);
        buttonResult = (TextView) findViewById(R.id.DateTimeRes);
        txtNewRem.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                txtNewRem.setFocusable(true);
                txtNewRem.setFocusableInTouchMode(true);
                return false;
            }
        });
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(ReminderProvider.CONTENT_ITEM_TYPE);


        Log.d("logging_tag", "id is  +**" + ReminderActivity.note_id);
        Log.d("logging_tag", "uri is  +**" + uri);
        if (uri == null)
        {
            action = Intent.ACTION_INSERT;
            setTitle(R.string.new_reminder_title);
        } else
        {
            setTitle(R.string.old_reminder_title);
            buttonDate.setText(R.string.new_reminder_date);
            buttonTime.setText(R.string.new_reminder_time);
            action = Intent.ACTION_EDIT;
            reminderFilter = DataBase.REMINDER_ID + "=" + uri.getLastPathSegment();
            Cursor cursor = getContentResolver().query(uri, DataBase.ALL_COLUMNS_REMINDER, reminderFilter, null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DataBase.REMINDER_TEXT));
            oldDateTime = cursor.getString(cursor.getColumnIndex(DataBase.REMINDER_ALARM_TIME));
            oldSpinnerCategory = cursor.getString(cursor.getColumnIndex(DataBase.REMINDER_CATEGORY));
            oldSpinnerRepeat = cursor.getString(cursor.getColumnIndex(DataBase.REMINDER_REPEAT_TIME));

            cursor.getInt(cursor.getColumnIndex(DataBase.REMINDER_REPEAT_TIME));
            buttonResult.setText(oldDateTime);
            txtNewRem.setText(oldText);
            txtNewRem.requestFocus();

            for (int i = 0; i < categories_compare.length; i++)
            {
                if (oldSpinnerCategory.equals(categories_compare[i]))
                {
                    spinner_category.setSelection(i);
                    break;
                }
                if (oldSpinnerRepeat.equals(reminder_repeat[i]))
                {
                    spinner_repeat.setSelection(i);
                    break;
                }
            }
        }
    }

    boolean creating = true;
    int id;
    String newText, newCategory, newTime, newRepeat;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.rem_save:
                creating = true;
                finishEditing();
                break;
            case R.id.rem_delete:
                if (action == Intent.ACTION_EDIT)
                {
                    deleteReminder();
                    id = 1;
                } else
                {
                    setResult(RESULT_CANCELED);
                }
                creating = false;
                break;
            case R.id.rem_discard:
                id = 3;
                finishEditing();
                break;
        }
        finish();
        return super.onOptionsItemSelected(item);
    }


    private void finishEditing()
    {
        newText = txtNewRem.getText().toString().trim();
        newTime = buttonResult.getText().toString().trim();
        newCategory = spinner_category.getSelectedItem().toString();
        newRepeat = spinner_repeat.getSelectedItem().toString();

        switch (action)
        {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0 && !creating)
                {
                    break;
                } else if (newText.length() == 0)
                {
                    setResult(RESULT_CANCELED);
                    break;
                } else if (id == 3)
                {
                    setResult(RESULT_CANCELED);
                } else
                {
                    /*insertReminder(newText, newTime, newCategory, newRepeat);*/
                    for (int i = 0; i < repeat.length; i++)
                    {
                        if (newRepeat.equals(repeat[i]))
                        {
                            newRepeat = String.valueOf(i);
                        }
                    }
                    int id_of_new_note = (int) dataBase.insertAlert(newText,
                            newTime, milissDate, newCategory, newRepeat);
                    createAlarm(id_of_new_note);
                    Log.d("logging_tag", "this shit" + id_of_new_note);
                    break;
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText.length() == 0)
                    deleteReminder();
                else if (newText.length() != 0 && id == 1)
                    break;
                else if (oldText.equals(newText))
                    setResult(RESULT_CANCELED);
                else if (id == 3)
                    setResult(RESULT_CANCELED);
                else
                    // alarm edit
                    updateReminder(newText, newTime, newCategory, newRepeat);
                break;
        }
        finish();
    }

 /*   private void insertReminder(String newText, String newTime, String newCategory, String newRepeat)
    {
        ContentValues values = new ContentValues();
        values.put(DataBase.REMINDER_TEXT, newText);
        values.put(DataBase.REMINDER_ALARM_TIME, newTime);
        values.put(DataBase.REMINDER_CATEGORY, newCategory);
        values.put(DataBase.REMINDER_REPEAT_TIME, newRepeat);
        //setRepeat();
        getContentResolver().insert(ReminderProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }*/

    private void updateReminder(String newText, String newTime, String newCategory, String newRepeat)
    {
        ContentValues values = new ContentValues();
        values.put(DataBase.REMINDER_TEXT, newText);
        values.put(DataBase.REMINDER_ALARM_TIME, newTime);
        values.put(DataBase.REMINDER_ALARM_TIME_MILIS, milissDate);
        values.put(DataBase.REMINDER_CATEGORY, newCategory);
        values.put(DataBase.REMINDER_REPEAT_TIME, newRepeat);
        getContentResolver().update(ReminderProvider.CONTENT_URI, values, reminderFilter, null);
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    public void deleteReminder()
    {
        getContentResolver().delete(ReminderProvider.CONTENT_URI, reminderFilter, null);
        Toast.makeText(this, "Reminder Deleted", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void createAlarm(int id)
    {
        Intent alarm = new Intent(this, AlarmService.class);
        alarm.putExtra("id", id);
        alarm.setAction(AlarmService.CREATE);
        startService(alarm);
        dataBase.close();
    }

    public void onClickTime(View v)
    {
        new TimePickerDialog(NewReminderActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    public void onClickDate(View v)
    {
        new DatePickerDialog(NewReminderActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener()
    {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
            milissDate = dateAndTime.getTimeInMillis();
        }
    };

    private void setInitialDateTime()
    {
        buttonResult.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_remind, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
