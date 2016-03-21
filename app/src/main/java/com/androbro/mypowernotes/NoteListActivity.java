package com.androbro.mypowernotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {

    private Menu mymenu;
    private DBHandler dba;
    private ArrayList<MyNote> dbNotes = new ArrayList<>();
    private NoteAdapter noteAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        listView = (ListView) findViewById(R.id.list);
        //fetching data to DB that we previuously saved
        refreshData();
    }

    private void refreshData() {
        dbNotes.clear();
        dba = new DBHandler(getApplicationContext());

        ArrayList<MyNote> notesFromDB = dba.getNotes();

        for (int i = 0; i < notesFromDB.size(); i++){

            String title = notesFromDB.get(i).getTitle();
            String dateText = notesFromDB.get(i).getRecordDate();
            String content = notesFromDB.get(i).getContent();
            int mid = notesFromDB.get(i).getItemId();


            MyNote myNote = new MyNote();
            myNote.setTitle(title);
            myNote.setContent(content);
            myNote.setRecordDate(dateText);
            myNote.setItemId(mid);

            dbNotes.add(myNote);
        }
        dba.close();

        //setup adapter
        noteAdapter = new NoteAdapter(NoteListActivity.this, R.layout.note_row, dbNotes);
        listView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notelist_menu, menu);
        mymenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_note){
            Intent intent = new Intent(NoteListActivity.this, NewNoteActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class NoteAdapter extends ArrayAdapter<MyNote> {
        Activity activity;
        int layoutResource;
        MyNote myNote;
        ArrayList<MyNote> mData = new ArrayList<>();

        public NoteAdapter(Activity act, int resource, ArrayList<MyNote> data) {
            super(act, resource, data);
            activity = act;
            layoutResource = resource;
            mData = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public MyNote getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getPosition(MyNote item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;
            ViewHolder holder = null;

            if (row == null || (row.getTag() == null)){
                LayoutInflater inflater = LayoutInflater.from(activity);
                row = inflater.inflate(layoutResource, null);
                holder = new ViewHolder();
                holder.mTitle = (TextView) row.findViewById(R.id.name);
                holder.mDate = (TextView) row.findViewById(R.id.dateText);

                row.setTag(holder);
            }else {
                holder = (ViewHolder) row.getTag();
            }

            holder.myNote = getItem(position);
            holder.mTitle.setText(holder.myNote.getTitle());
            holder.mDate.setText(holder.myNote.getRecordDate());
            holder.setmId(holder.myNote.getItemId());

            final ViewHolder finalHolder = holder;
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = finalHolder.myNote.getContent().toString();
                    String dateText = finalHolder.myNote.getRecordDate().toString();
                    String title = finalHolder.myNote.getTitle().toString();

                    int mid = finalHolder.myNote.getItemId();
                    Intent i = new Intent(NoteListActivity.this, NoteDetailActivity.class);

                    i.putExtra("content", text);
                    i.putExtra("title", title);
                    i.putExtra("date", dateText);
                    i.putExtra("id", mid);
                    startActivity(i);
                }
            });

            return row;
        }

        class ViewHolder{
            MyNote myNote;
            TextView mTitle;

            public int getmId() {
                return mId;
            }

            public void setmId(int mId) {
                this.mId = mId;
            }

            int mId;
            TextView mContent;
            TextView mDate;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NoteListActivity.this, LoginActivity.class);
        startActivity(i);
    }

}
