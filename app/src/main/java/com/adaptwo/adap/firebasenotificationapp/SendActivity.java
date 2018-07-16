package com.adaptwo.adap.firebasenotificationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {

    private TextView user_id_view;

    private String mUserId;
    private String Commands;
    private String mUserName;
    private String mCurrentId;
    private String time;
    private String type;

    private EditText mMessageView;
    private Button mSendBtn;
    private Button mPositive;
    private Button mCorrective;
    private ProgressBar mMessageProgress;

    private FirebaseFirestore mFirestore;
    private Spinner mPositiveSpinner;
    private Spinner mCorrectiveSpinner;
    ArrayAdapter<CharSequence> mPositiveAdapter;
    ArrayAdapter<CharSequence> mCorrectiveAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);


        mPositiveSpinner = (Spinner) findViewById(R.id.positiveSpinner);
        mPositiveAdapter = ArrayAdapter.createFromResource(this, R.array.positiveVibrations,
                android.R.layout.simple_spinner_item);
        mPositiveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPositiveSpinner.setAdapter(mPositiveAdapter);

        mCorrectiveSpinner = (Spinner) findViewById(R.id.correctiveSpinner);
        mCorrectiveAdapter = ArrayAdapter.createFromResource(this, R.array.correctiveVibrations,
                android.R.layout.simple_spinner_item);
        mCorrectiveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCorrectiveSpinner.setAdapter(mCorrectiveAdapter);

        mPositiveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int positiveChoice = parent.getSelectedItemPosition();
                SharedPreferences sharedPref = getSharedPreferences("FileName", 0);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putInt("positiveChoice", positiveChoice);
                prefEditor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        SharedPreferences sharedPref = getSharedPreferences("FileName", MODE_PRIVATE);
        int spinnerValue = sharedPref.getInt("positiveChoice", -1);
        if (spinnerValue != -1) {
            // set the selected value of the spinner
            mPositiveSpinner.setSelection(spinnerValue);
        }
        Log.d("NotificationsApp", "Spinner Positive selection: " + spinnerValue);

        mCorrectiveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int correctiveChoice = parent.getSelectedItemPosition();
                SharedPreferences sharedPrefCor = getSharedPreferences("FileNameCor", 0);
                SharedPreferences.Editor prefEditorCor = sharedPrefCor.edit();
                prefEditorCor.putInt("correctiveChoice", correctiveChoice);
                prefEditorCor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SharedPreferences sharedPrefCor = getSharedPreferences("FileNameCor", MODE_PRIVATE);
        int spinnerValueCor = sharedPrefCor.getInt("correctiveChoice", -1);
        if (spinnerValue != -1) {
            // set the selected value of the spinner
            mCorrectiveSpinner.setSelection(spinnerValueCor);
        }


        user_id_view = (TextView) findViewById(R.id.user_name_view);
        mMessageView = (EditText) findViewById(R.id.message_view);
        mSendBtn = (Button) findViewById(R.id.send_btn);
        mPositive = findViewById(R.id.positive);
        mCorrective = findViewById(R.id.corrective);
        mMessageProgress = (ProgressBar) findViewById(R.id.messageProgress);

        mFirestore = FirebaseFirestore.getInstance();
        mCurrentId = FirebaseAuth.getInstance().getUid();

//        FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Log.d("NotificationApp", "From UID: " + mCurrentId);

        time = Calendar.getInstance().getTime().toString();
        mUserId = getIntent().getStringExtra("user_id");
        mUserName = getIntent().getStringExtra("user_name");
        Log.d("NotificationApp", "User ID: " + mUserId);
        Commands = "Commands";
        final int launcher = R.mipmap.ic_launcher;
//comment011
        user_id_view.setText("Send to " + mUserName);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String message = mMessageView.getText().toString();
                type = "Custom";
                Log.d("NotificationsApp", "Launcher: " + launcher);

                if (!TextUtils.isEmpty(message)) {

                    mMessageProgress.setVisibility(View.VISIBLE);

                    Map<String, Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("message", message);
                    notificationMessage.put("from", mCurrentId);
                    notificationMessage.put("to", mUserName);
                    notificationMessage.put("time", time);
                    notificationMessage.put("messageType", type);
                    notificationMessage.put("launcher", launcher);

//                    notificationMessage.put("vibrationTime", vibrationTime);
//                    notificationMessage.put("vibrationAmp", vibrationAmp);

                    //Format of storing in Firestore: mFirestore.collection("Collection" + Document +
                    // "Collection" + Document)
                    mFirestore.collection("Users/" + mUserId + "/Notifications").
                            add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(SendActivity.this, "Notification Sent.", Toast.LENGTH_LONG).show();
                            mMessageView.setText("");
                            mMessageProgress.setVisibility(View.INVISIBLE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(SendActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                            mMessageProgress.setVisibility(View.INVISIBLE);

                        }
                    });

                }

            }
        });
        mPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = "Good Job! \uD83D\uDE00";
                type = "Positive";

                if (!TextUtils.isEmpty(message)) {

                    mMessageProgress.setVisibility(View.VISIBLE);

                    Map<String, Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("message", message);
                    notificationMessage.put("from", mCurrentId);
                    notificationMessage.put("to", mUserName);
                    notificationMessage.put("time", time);
                    notificationMessage.put("messageType", type);
                    notificationMessage.put("launcher", launcher);


                    //Format of storing in Firestore: mFirestore.collection("Collection" + Document + "Collection" + Document)
                    mFirestore.collection("Users/" + mUserId + "/Notifications").
                            add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(SendActivity.this, "Notification Sent.", Toast.LENGTH_LONG).show();
                            mMessageProgress.setVisibility(View.INVISIBLE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(SendActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                            mMessageProgress.setVisibility(View.INVISIBLE);

                        }
                    });

                }

            }
        });

        mCorrective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = "Don't do that! \uD83D\uDE41";
                type = "Corrective";

                if (!TextUtils.isEmpty(message)) {

                    mMessageProgress.setVisibility(View.VISIBLE);

                    Map<String, Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("message", message);
                    notificationMessage.put("from", mCurrentId);
                    notificationMessage.put("to", mUserName);
                    notificationMessage.put("time", time);
                    notificationMessage.put("messageType", type);
                    notificationMessage.put("launcher", launcher);


                    //Format of storing in Firestore: mFirestore.collection("Collection" + Document + "Collection" + Document)
                    mFirestore.collection("Users/" + mUserId + "/Notifications").
                            add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(SendActivity.this, "Notification Sent.", Toast.LENGTH_LONG).show();
                            mMessageProgress.setVisibility(View.INVISIBLE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(SendActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                            mMessageProgress.setVisibility(View.INVISIBLE);

                        }
                    });

                }

            }
        });

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        Log.d("TAG", "action: " + action);
        String type = intent.getType();
        Log.d("TAG", "type: " + type);


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            Log.d("TAG", "Gettting intent...");
            handleSendText(intent); // Handle text being sent
        } else {
            // Handle other intents, such as being started from the home screen
            Log.d("TAG", "Token not sent to phone");
        }

    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            Log.d("TAG", "Watch token on phone is: " + sharedText);
        }
   }


}
