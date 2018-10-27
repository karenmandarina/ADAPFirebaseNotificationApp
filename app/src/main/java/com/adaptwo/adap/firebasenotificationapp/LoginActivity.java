package com.adaptwo.adap.firebasenotificationapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn;
    private Button mRegPageBtn;

    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("NotificationApp", "login onCreate step 1");

        mEmailField = (EditText) findViewById(R.id.login_email);
        mPasswordField = (EditText) findViewById(R.id.login_password);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mRegPageBtn = (Button) findViewById(R.id.login_register_btn);

        mProgressBar = (ProgressBar) findViewById(R.id.loginProgress);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        Log.d("NotificationApp", "login onCreate step 2");


        mRegPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToRegister();

            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                    mProgressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){


                                String token_id = FirebaseInstanceId.getInstance().getToken();
                                // String current_id = mAuth.getCurrentUser().getUid();
                                String current_email = mAuth.getCurrentUser().getEmail();

                                // Upon logging in we get the Token ID of the devise the user is logging into
                                // this way we can send notification to that devise via the Token ID
                                Map<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("token_id", token_id);

                                mFirestore.collection("Users").document(current_email)
                                        .update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        sendToMain();
                                        mProgressBar.setVisibility(View.INVISIBLE);

                                    }
                                });

                            } else {

                                Toast.makeText(LoginActivity.this, "Error : " +
                                        task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                mProgressBar.setVisibility(View.INVISIBLE);

                            }

                        }
                    });

                }

            }
        });

        Log.d("NotificationApp", "login onCreate step 3");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("NotificationApp", "Login page step 4");

        if(currentUser != null){
            Log.d("NotificationApp", "Login page to main. Current user is: " + currentUser);
            sendToMain();
            Log.d("NotificationApp", "Main");

        } else {
            Log.d("NotificationApp", "Current user is logged out. This should be null: " + currentUser);

        }

    }

    private void sendToMain() {

        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
        Log.d("NotificationApp", "login onCreate step 5");

    }
    private void sendToRegister(){
        Intent regIntent = new Intent(LoginActivity.this,
                com.adaptwo.adap.firebasenotificationapp.RegisterActivity.class);
        finish();
        startActivity(regIntent);

    }

}
