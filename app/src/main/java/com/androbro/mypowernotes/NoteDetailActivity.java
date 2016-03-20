package com.androbro.mypowernotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NoteDetailActivity extends AppCompatActivity {

    private TextView title, date, content;
    private Button deleteButton;

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
            final int id = extras.getInt("id");

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHandler dba = new DBHandler(getApplicationContext());
                    dba.deleteNote(id);
                    Toast.makeText(getApplicationContext(), "Note deleted!", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(NoteDetailActivity.this, NoteListActivity.class));
                }
            });

        }
    }
}
