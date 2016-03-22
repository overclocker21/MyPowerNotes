package com.androbro.mypowernotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NewNoteActivity extends AppCompatActivity {

    private Menu mymenu;
    private EditText title;
    private EditText content;
    private DBHandler dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        dba = new DBHandler(NewNoteActivity.this);

        title = (EditText) findViewById(R.id.titleEditText);
        content = (EditText) findViewById(R.id.noteEditText);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newnote_menu, menu);
        mymenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_note){

            Toast.makeText(getApplicationContext(), "Your note was saved", Toast.LENGTH_LONG).show();
            saveToDB();
            Intent i = new Intent(NewNoteActivity.this, NoteListActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveToDB();
        Intent i = new Intent(NewNoteActivity.this, NoteListActivity.class);
        startActivity(i);

    }

    private void saveToDB() {

        MyNote note = new MyNote();

        //check for null title

            note.setTitle(title.getText().toString().trim());
            note.setContent(content.getText().toString().trim());

        dba.addNotes(note);
        dba.close();

        //clear the forms once user clicks the save button
        title.setText("");
        content.setText("");

    }
}
