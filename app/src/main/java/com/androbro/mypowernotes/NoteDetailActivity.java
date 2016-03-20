package com.androbro.mypowernotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NoteDetailActivity extends AppCompatActivity {

    private Menu mymenu;
    private TextView title, date, content;
    private Button deleteButton;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        title = (TextView) findViewById(R.id.detailsTitle);
        date = (TextView) findViewById(R.id.detailsDateText);
        content = (TextView) findViewById(R.id.detailsTextView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title.setText(extras.getString("title"));
            date.setText("Created: " + extras.getString("date"));
            content.setText("\"" + extras.getString("content") + " \" ");
            noteId = extras.getInt("id");

        }
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

            DBHandler dba = new DBHandler(getApplicationContext());
            dba.deleteNote(noteId);
            Toast.makeText(getApplicationContext(), "Note deleted!", Toast.LENGTH_LONG).show();

            startActivity(new Intent(NoteDetailActivity.this, NoteListActivity.class));

            return true;
        } else if (id == R.id.edit_note){

//            Intent intent = new Intent(NoteDetailActivity.this, NewNoteActivity.class);
//            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
