package com.androbro.mypowernotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewNoteActivity extends AppCompatActivity {

    private EditText title;
    private EditText content;
    private Button saveBtn;
    private Button showList;
    private DBHandler dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        dba = new DBHandler(NewNoteActivity.this);

        title = (EditText) findViewById(R.id.titleEditText);
        content = (EditText) findViewById(R.id.noteEditText);
        saveBtn = (Button) findViewById(R.id.saveButton);
        showList = (Button) findViewById(R.id.showList);

        showList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewNoteActivity.this, NoteListActivity.class);
                startActivity(i);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDB();
                Toast.makeText(getApplicationContext(), "Your note was saved", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveToDB() {

        MyNote note = new MyNote();
        note.setTitle(title.getText().toString().trim());
        note.setContent(content.getText().toString().trim());

        dba.addNotes(note);
        dba.close();

        //clear the forms once user clicks the save button
        title.setText("");
        content.setText("");

        Intent i = new Intent(NewNoteActivity.this, NoteListActivity.class);
        startActivity(i);
    }
}
