package com.androbro.mypowernotes;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class DrawingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //custom drawing view
    private DrawingView drawView;
    private ImageButton currPaint;

    //sizes
    private float smallBrush, mediumBrush, largeBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get drawing view
        drawView = (DrawingView)findViewById(R.id.drawing);

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.color_picker, null);
        currPaint = (ImageButton)dialogView.findViewById(R.id.color1);

        //sizes from dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        //set initial size
        drawView.setSaveEnabled(true);
        drawView.setBrushSize(smallBrush);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(DrawingActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DrawingActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(DrawingActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2909);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.new_btn) {

            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();

        } else if (id == R.id.draw_btn) {

            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //listen for clicks on size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            //show and wait for user interaction
            brushDialog.show();

        } else if (id == R.id.erase_btn) {

            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();

        } else if (id == R.id.opacity_btn) {

            //launch opacity chooser
            final Dialog seekDialog = new Dialog(this);
            seekDialog.setTitle("Opacity level:");
            seekDialog.setContentView(R.layout.opacity_chooser);
            //get ui elements
            final TextView seekTxt = (TextView)seekDialog.findViewById(R.id.opq_txt);
            final SeekBar seekOpq = (SeekBar)seekDialog.findViewById(R.id.opacity_seek);
            //set max
            seekOpq.setMax(100);
            //show current level
            int currLevel = drawView.getPaintAlpha();
            seekTxt.setText(currLevel+"%");
            seekOpq.setProgress(currLevel);
            //update as user interacts
            seekOpq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekTxt.setText(Integer.toString(progress)+"%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}

            });
            //listen for clicks on ok
            Button opqBtn = (Button)seekDialog.findViewById(R.id.opq_ok);
            opqBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setPaintAlpha(seekOpq.getProgress());
                    seekDialog.dismiss();
                }
            });
            //show dialog
            seekDialog.show();

        } else if (id == R.id.save_btn) {

            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.System.canWrite(DrawingActivity.this)) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);

                            //save drawing
                            drawView.setDrawingCacheEnabled(true);
                            //attempt to save
                            String imgSaved = MediaStore.Images.Media.insertImage(
                                    getContentResolver(), drawView.getDrawingCache(),
                                    UUID.randomUUID().toString() + ".png", "drawing");
                            //feedback
                            if (imgSaved != null) {
                                Toast savedToast = Toast.makeText(getApplicationContext(),
                                        "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                                savedToast.show();
                            } else {
                                Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                                unsavedToast.show();
                            }
                            drawView.destroyDrawingCache();
                        }

                    }else{
                        //save drawing
                        drawView.setDrawingCacheEnabled(true);
                        //attempt to save
                        String imgSaved = MediaStore.Images.Media.insertImage(
                                getContentResolver(), drawView.getDrawingCache(),
                                UUID.randomUUID().toString() + ".png", "drawing");
                        //feedback
                        if (imgSaved != null) {
                            Toast savedToast = Toast.makeText(getApplicationContext(),
                                    "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                            savedToast.show();
                        } else {
                            Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                    "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                            unsavedToast.show();
                        }
                        drawView.destroyDrawingCache();
                    }
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();


        } else if (id == R.id.pick_color) {

            //switch to erase - choose size
            final Dialog colorDialog = new Dialog(this);
            colorDialog.setTitle("Pick a color:");
            colorDialog.setContentView(R.layout.color_picker);
            //listen for clicks on size buttons
            ImageButton color1 = (ImageButton)colorDialog.findViewById(R.id.color1);
            ImageButton color2 = (ImageButton)colorDialog.findViewById(R.id.color2);
            ImageButton color3 = (ImageButton)colorDialog.findViewById(R.id.color3);
            ImageButton color4 = (ImageButton)colorDialog.findViewById(R.id.color4);
            ImageButton color5 = (ImageButton)colorDialog.findViewById(R.id.color5);
            ImageButton color6 = (ImageButton)colorDialog.findViewById(R.id.color6);
            ImageButton color7 = (ImageButton)colorDialog.findViewById(R.id.color7);
            ImageButton color8 = (ImageButton)colorDialog.findViewById(R.id.color8);
            ImageButton color9 = (ImageButton)colorDialog.findViewById(R.id.color9);
            ImageButton color10 = (ImageButton)colorDialog.findViewById(R.id.color10);
            ImageButton color11 = (ImageButton)colorDialog.findViewById(R.id.color11);
            ImageButton color12 = (ImageButton)colorDialog.findViewById(R.id.color12);

            color1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paintClicked(v);
                    colorDialog.dismiss();
                }
            });

            color2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paintClicked(v);
                    colorDialog.dismiss();
                }
            });

            color3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paintClicked(v);
                    colorDialog.dismiss();
                }
            });

            color4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paintClicked(v);
                    colorDialog.dismiss();
                }
            });

            color5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paintClicked(v);
                    colorDialog.dismiss();
                }
            });

            color6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paintClicked(v);
                    colorDialog.dismiss();
                }
            });

            color7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paintClicked(v);
                    colorDialog.dismiss();
                }
            });

            color8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paintClicked(v);
                    colorDialog.dismiss();
                }
            });

            color9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paintClicked(v);
                    colorDialog.dismiss();
                }
            });

            color10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paintClicked(v);
                    colorDialog.dismiss();
                }
            });

            color11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paintClicked(v);
                    colorDialog.dismiss();
                }
            });

            color12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    paintClicked(v);
                    colorDialog.dismiss();
                }
            });

            colorDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void paintClicked(View view) {

        drawView.setErase(false);
        drawView.setPaintAlpha(100);
        drawView.setBrushSize(drawView.getLastBrushSize());

        if(view!=currPaint){
            String color = view.getTag().toString();
            drawView.setColor(color);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }
}
