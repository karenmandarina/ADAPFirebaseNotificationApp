package com.adaptwo.adap.firebasenotificationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationActivity extends AppCompatActivity {

    private TextView mNotifData;
    private Button mShowNotifs;
    private FirebaseFirestore mFirestore;
    // to receive notification while app is closed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mFirestore = FirebaseFirestore.getInstance();
        mNotifData = (TextView) findViewById(R.id.notif_text);
        mShowNotifs = (Button) findViewById(R.id.show_notif);

        // changed it to give the name instead of UID
        String dataFrom = getIntent().getStringExtra("from_user_id");
        String dataMessage = getIntent().getStringExtra("message");

        mNotifData.setText(" FROM : " + dataFrom + " | MESSAGE : " + dataMessage);


        mShowNotifs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
