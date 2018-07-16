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

        String dataFrom = getIntent().getStringExtra("from_user_id");
        String dataMessage = getIntent().getStringExtra("message");


        mFirestore.collection("Users").document(dataFrom).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String from_name = documentSnapshot.getString("name");

                SharedPreferences Pref = getSharedPreferences("FromName",0);
                SharedPreferences.Editor prefEdit = Pref.edit();
                prefEdit.putString("fromName",from_name);
                prefEdit.commit();
            }
        });

        SharedPreferences Pref = getSharedPreferences("FromName",MODE_PRIVATE);
        String  from_name = Pref.getString("fromName",null);
        if(from_name != null) {
            // set the selected value of the spinner
            mNotifData.setText(" FROM : " + from_name + " | MESSAGE : " + dataMessage);
        }

        Log.d("NotificationsApp", "Message: " + dataMessage);


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
