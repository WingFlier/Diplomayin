package com.example.home.diplom.view.NoteActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.home.diplom.R;
import com.example.home.diplom.model.DataBase;
import com.example.home.diplom.presenter.provider.NotesProvider;

public class NewNoteActivity extends AppCompatActivity {

    //to remember what im doing. to create or update note
    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editor = (EditText) findViewById(R.id.editText);
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("new Note");
        }else{
            action = Intent.ACTION_EDIT;
            noteFilter = DataBase.NOTE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri, DataBase.ALL_COLUMNS, noteFilter,
                    null,null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DataBase.NOTE_TEXT));
            editor.setText(oldText);
            editor.requestFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.note_delete:
                deleteNote();
                break;
            case R.id.note_discard:
                setResult(RESULT_CANCELED);
        }
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void finishEditing() {
        String newText = editor.getText().toString().trim();

        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    insertNote(newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText.length() == 0){
                    deleteNote();
                }else if(oldText.equals(newText)){
                    setResult(RESULT_CANCELED);
                }else{
                    updateNote(newText);
                }
        }
        finish();


    }

    private void deleteNote() {
        getContentResolver().delete(NotesProvider.CONTENT_URI,noteFilter,null);
        Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DataBase.NOTE_TEXT, note);
        getContentResolver().update(NotesProvider.CONTENT_URI,values, noteFilter, null);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void insertNote(String note) {
        ContentValues values = new ContentValues();
        values.put(DataBase.NOTE_TEXT, note);
        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }

    @Override
    protected void onStop() {
        finishEditing();
        super.onStop();
    }
}
