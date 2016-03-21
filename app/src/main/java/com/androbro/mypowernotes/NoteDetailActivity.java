package com.androbro.mypowernotes;

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
        if (extras != null) {
            title.setText(extras.getString("title"));
            date.setText("Created: " + extras.getString("date"));
            content.setText(extras.getString("content"));
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

            dba.deleteNote(noteId);
            Toast.makeText(getApplicationContext(), "Note deleted!", Toast.LENGTH_LONG).show();

            startActivity(new Intent(NoteDetailActivity.this, NoteListActivity.class));

            return true;
        } else if (id == R.id.edit_note){

            editHint.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);

            title.setKeyListener((KeyListener) title.getTag());

            content.setKeyListener((KeyListener) content.getTag());

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
