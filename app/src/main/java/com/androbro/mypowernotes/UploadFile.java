package com.androbro.mypowernotes;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by user on 3/20/2016.
 */
public class UploadFile extends AsyncTask<Void, Void, Boolean> {

    private DropboxAPI dropboxAPI;
    private String path;
    private Context context;
    private String fileName;

    public UploadFile(Context context, DropboxAPI dropboxAPI, String path, String fileName) {
        this.dropboxAPI = dropboxAPI;
        this.path = path;
        this.context = context;
        this.fileName = fileName;
    }


    @Override
    protected Boolean doInBackground(Void... params) {

        try{

            FileInputStream fis = context.openFileInput("text.txt");
            dropboxAPI.putFile(path + fileName, fis,
                    fis.getChannel().size(), null, null);

            return true;

        } catch (IOException e){

        } catch (DropboxException de){

        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result){
            Toast.makeText(context, "File has been uploaded", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, "Error occured!", Toast.LENGTH_LONG).show();
        }
    }
}
