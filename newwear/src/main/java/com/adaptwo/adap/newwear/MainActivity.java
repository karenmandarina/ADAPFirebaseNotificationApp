package com.adaptwo.adap.newwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    private TextView mProfileLabel;
    private TextView mNotificationLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProfileLabel = (TextView) findViewById(R.id.profileLabel);
        mNotificationLabel = (TextView) findViewById(R.id.notificationsLabel);


        mProfileLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent profile = new Intent(MainActivity.this, ProfilePageView.class);
                finish();
                startActivity(profile);
                Log.d("NotificationApp", "Moved to profile view");


            }
        });


        mNotificationLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent notif = new Intent(MainActivity.this, NotificationPageView.class);
                finish();
                startActivity(notif);
                Log.d("NotificationApp", "Moved to notification view");


            }
        });

    }
}