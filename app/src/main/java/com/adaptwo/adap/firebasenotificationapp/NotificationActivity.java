package com.adaptwo.adap.firebasenotificationapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    private TextView mNotifData;
// to receive notification while app is closed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        String dataMessage = getIntent().getStringExtra("message");
        String dataFrom = getIntent().getStringExtra("from_user_id");

        mNotifData = (TextView) findViewById(R.id.notif_text);

        mNotifData.setText(" FROM : " + dataFrom + " | MESSAGE : " + dataMessage);


    }
}
