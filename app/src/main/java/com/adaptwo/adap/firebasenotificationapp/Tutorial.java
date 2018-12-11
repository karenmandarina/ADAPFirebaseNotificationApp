package com.adaptwo.adap.firebasenotificationapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by karenmandarina on 7/18/18.
 */

public class Tutorial extends AppCompatActivity {
    //Declare play button
    Button play;
    //Decalare video viewing area
    VideoView vid;
    //Declare media controller
    MediaController med;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("NotificationApp", "Onto Tutorial class  ");

        setContentView(R.layout.activity_tutorial);
        //Define play button
        play = (Button) findViewById(R.id.button3);
        //Define video viewing area
        vid = (VideoView) findViewById(R.id.vidview);
        //Define media controller
        med = new MediaController(this);
    }

    //this code brings the user back to the main menu when the back button is pressed
    public void tuttomain(View view){
        Intent homeIntent = new Intent(Tutorial.this, LoginActivity.class);
        startActivity(homeIntent);
        finish();
    }

    //button onclick method that executes with each press of 'play' button
    public void playvid(View view){
        //Create the path within the application where the video is located
        String vidpath = "android.resource://com.adaptwo.adap.firebasenotificationapp/" + R.raw.tut;
        //Create Uri path from above string that contains the location of the tutorial video
        Uri uri = Uri.parse(vidpath);
        //sets videoview to whatever file (video in this cas) is defined by the Uri path
        vid.setVideoURI(uri);
        //associates the med media controller with the vid videoview
        vid.setMediaController(med);
        //specifies where the controls will be seen. in this case it is over the video
        med.setAnchorView(vid);
        //make the play button invisible
        play.setVisibility(View.INVISIBLE);
        //start the video
        vid.start();
    }
}
