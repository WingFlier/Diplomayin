package com.example.home.diplom.view.NoteActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;
import com.example.home.diplom.presenter.provider.Note.NotesProvider;

public class NewNoteActivity extends AppCompatActivity
{

    //to remember what im doing. to create or update note
    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldText;
    private FloatingActionButton fab_new_note_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editor = (EditText) findViewById(R.id.editText);
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);
        fab_new_note_edit = (FloatingActionButton) findViewById(R.id.fab_new_note);

        fab_new_note_edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fab_new_note_edit.setVisibility(View.GONE);
                editor.setFocusable(true);
                editor.setFocusableInTouchMode(true);
                editor.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editor, InputMethodManager.SHOW_FORCED);


            }
        });


        if (uri == null)
        {
            action = Intent.ACTION_INSERT;
            setTitle(R.string.new_note_activity);
        } else
        {
            setTitle(R.string.old_note_activity);
            action = Intent.ACTION_EDIT;
            Log.d("str", "uri else worked: noteFilter is:" + noteFilter);
            noteFilter = DataBase.NOTE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, DataBase.ALL_COLUMNS_NOTE, noteFilter,
                    null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DataBase.NOTE_TEXT));
            editor.setText(oldText);
            editor.requestFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_note, menu);
        return true;
    }

    String newText;
    boolean created = true;
    int id;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        newText = editor.getText().toString().trim();
        switch (item.getItemId())
        {
            case R.id.note_save:
                created = true;
                finishEditing();
                break;
            case R.id.note_delete:
                if (action == Intent.ACTION_EDIT)
                {
                    deleteNote();
                    id = 1;
                } else
                    setResult(RESULT_CANCELED);
                created = false;
                break;
            case R.id.note_discard:
                id = 3;
                finishEditing();
                break;
        }
        ((InputMethodManager) NewNoteActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(editor
                        .getWindowToken(), 0);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void finishEditing()
    {
        newText = editor.getText().toString().trim();


        switch (action)
        {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0)
                {
                    setResult(RESULT_CANCELED);
                } else if (newText.length() != 0 && !created)
                {
                    break;
                } else if (id == 3)
                {
                    setResult(RESULT_CANCELED);
                } else
                {
                    insertNote(newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText.length() == 0)
                {
                    deleteNote();
                } else if (newText.length() != 0 && id == 1)
                {
                    break;
                } else if (oldText.equals(newText))
                {
                    setResult(RESULT_CANCELED);
                } else if (id == 3)
                {
                    setResult(RESULT_CANCELED);
                } else
                {
                    updateNote(newText);
                }
                break;
        }
        finish();


    }

    private void deleteNote()
    {
        getContentResolver().delete(NotesProvider.CONTENT_URI, noteFilter, null);
        Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateNote(String note)
    {
        ContentValues values = new ContentValues();
        values.put(DataBase.NOTE_TEXT, note);
        getContentResolver().update(NotesProvider.CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String note)
    {
        ContentValues values = new ContentValues();
        values.put(DataBase.NOTE_TEXT, note);
        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onStop()
    {
        setResult(RESULT_CANCELED);
        finish();
        super.onStop();
    }


}
