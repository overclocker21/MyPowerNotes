package com.androbro.mypowernotes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NoteDetailActivity extends AppCompatActivity {

    private Menu mymenu;
    private DBHandler dba;
    private Button saveButton;
    private int noteId;

    private TextView date;

    private EditText title;
    private EditText content;
    private TextView editHint;

    private String noteContent;
    private String noteTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        editHint = (TextView) findViewById(R.id.editHint);

        saveButton = (Button) findViewById(R.id.saveButton);
        title = (EditText) findViewById(R.id.titleEditText);
        date = (TextView) findViewById(R.id.detailsDateText);
        content = (EditText) findViewById(R.id.noteEditText);

        dba = new DBHandler(getApplicationContext());

        title.setTag(title.getKeyListener());
        title.setKeyListener(null);

        content.setTag(content.getKeyListener());
        content.setKeyListener(null);

        Bundle extras = getIntent().getExtras();
        noteContent = extras.getString("content");
        noteTitle = extras.getString("title");

        if (extras != null) {
            title.setText(noteTitle);
            date.setText("Created: " + extras.getString("date"));
            content.setText(noteContent);
            noteId = extras.getInt("id");
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            saveToDB();


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notedetail_menu, menu);
        mymenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delete_note){

            AlertDialog.Builder adb=new AlertDialog.Builder(NoteDetailActivity.this);
            adb.setTitle("Delete?");
            adb.setMessage("Are you sure you want to delete this note?");
            adb.setNegativeButton("Cancel", null);
            adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dba.deleteNote(noteId);
                    Toast.makeText(getApplicationContext(), "Note deleted!", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(NoteDetailActivity.this, NoteListActivity.class));

                }
            });
            adb.show();

            return true;
        } else if (id == R.id.edit_note){

            editHint.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);

            title.setKeyListener((KeyListener) title.getTag());

            content.setKeyListener((KeyListener) content.getTag());

            return true;
        } else if (id == R.id.sendToDrobBox){

            Intent i = new Intent(NoteDetailActivity.this, DropBoxActivity.class);
            i.putExtra("content", noteContent);
            startActivity(i);

            return true;
        } else if (id == R.id.sendByEmail){

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, noteContent);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveToDB() {
        MyNote note = new MyNote();
        note.setTitle(title.getText().toString().trim());
        note.setContent(content.getText().toString().trim());

            dba.updateNotes(note, noteId);
            dba.close();

            Toast.makeText(getApplicationContext(), "Note updated!", Toast.LENGTH_LONG).show();

            Intent i = new Intent(NoteDetailActivity.this, NoteListActivity.class);
            startActivity(i);

    }

}
