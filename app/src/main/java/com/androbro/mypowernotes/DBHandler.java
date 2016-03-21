package com.androbro.mypowernotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 3/19/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    private final ArrayList<MyNote> noteList = new ArrayList<>();

    public DBHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create our tables here:

        String CREATE_NOTES_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.TITLE_NAME +
                " TEXT, " + Constants.CONTENT_NAME + " TEXT, " + Constants.DATE_NAME + " LONG);";
        db.execSQL(CREATE_NOTES_TABLE);

        Log.v("DB", "DB created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TITLE_NAME);
        //create a new one
        onCreate(db);
    }

    //delete note
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ? ", new String[]{String.valueOf(id)});
        db.close();
    }


    //add content to table
    public void addNotes(MyNote note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();//key-value pair data structure
        values.put(Constants.TITLE_NAME, note.getTitle());
        values.put(Constants.CONTENT_NAME, note.getContent());
        values.put(Constants.DATE_NAME, java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME, null, values);
        db.close();
    }

    public void updateNotes(MyNote note, int id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();//key-value pair data structure
        values.put(Constants.TITLE_NAME, note.getTitle());
        values.put(Constants.CONTENT_NAME, note.getContent());
        values.put(Constants.DATE_NAME, java.lang.System.currentTimeMillis());

        db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=" + id, null );
        db.close();

    }

    //get all notes

    public ArrayList<MyNote> getNotes() {

        String selectQuery = "SELECT * FROM " + Constants.TITLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        //creating a Cursor - object that points to our table content
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID,
                        Constants.TITLE_NAME, Constants.CONTENT_NAME, Constants.DATE_NAME}, null, null, null, null,
                Constants.DATE_NAME + " DESC");

        //loop through cursor
        if (cursor.moveToFirst()) {
            do {
                //while we loop through we po[ulating our note object
                MyNote myNote = new MyNote();
                myNote.setTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE_NAME)));
                myNote.setContent(cursor.getString(cursor.getColumnIndex(Constants.CONTENT_NAME)));
                myNote.setItemId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));


                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String dataData = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_NAME))).getTime());
                myNote.setRecordDate(dataData);

                noteList.add(myNote);

            } while (cursor.moveToNext());
        }

        return noteList;
    }
}
