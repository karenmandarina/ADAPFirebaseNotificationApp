package com.adaptwo.adap.firebasenotificationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final int PICK_IMAGE = 1;
    private CircleImageView mImageBtn;
    private EditText mEmailField;
    private EditText mNameField;
    private EditText mPasswordField;
    private Button mRegBtn;
    private Button mLoginPageBtn;

    private Uri imageUri;
    private String mStatus;

    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Spinner spinner;
    ArrayAdapter<CharSequence> spinnerAdapter;
    private ProgressBar mRegisterProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageUri = null;
        mStorage = FirebaseStorage.getInstance().getReference().child("images");
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        spinner = (Spinner) findViewById(R.id.question);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.options,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        mImageBtn = (CircleImageView) findViewById(R.id.register_image_btn);
        mEmailField = (EditText) findViewById(R.id.register_email);
        mPasswordField = (EditText) findViewById(R.id.register_password);
        mNameField = (EditText) findViewById(R.id.register_name);
        mRegBtn = (Button) findViewById(R.id.register_btn);
        mLoginPageBtn = (Button) findViewById(R.id.register_login_btn);
        mRegisterProgressBar = (ProgressBar) findViewById(R.id.registerProgressBar);

        // Advise: create a different class for this. Right now, profile pic is mandatory. If the user closes app
        // half way through registration, an account will still be created.
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });

        mLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToLogin();
            }
        });

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

// Melly's code:

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
                int status = parent.getSelectedItemPosition();
                SharedPreferences sharedPrefStatus = getSharedPreferences("FileNameCor", 0);
                SharedPreferences.Editor prefEditorStatus = sharedPrefStatus.edit();
                prefEditorStatus.putInt("status", status);
                prefEditorStatus.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
    private void sendToLogin(){
        Intent logIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(logIntent);
        finish();

    }
    private void uploadImage(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }





    private void createNewAccount(){

        if(imageUri != null){

            final String name = mNameField.getText().toString();
            final String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();

            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                // receving the value of the spinner
                SharedPreferences sharedPrefStatus = getSharedPreferences("FileNameCor", MODE_PRIVATE);
                final int spinnerValueStatus = sharedPrefStatus.getInt("status", -1);
                if (spinnerValueStatus > 0) {

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                mRegisterProgressBar.setVisibility(View.VISIBLE);

                                //final String user_id = mAuth.getCurrentUser().getUid();
                                final String user_email = mAuth.getCurrentUser().getEmail();

                                final StorageReference user_profile = mStorage.child(user_email + ".jpg");

                                user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {

                                        if (uploadTask.isSuccessful()) {

                                            //final String download_urlx = uploadTask.getResult().getDownloadUrl().toString();
                                            user_profile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    final String download_url = uri.toString();
                                                    String token_id = FirebaseInstanceId.getInstance().getToken();

                                                    if (spinnerValueStatus == 1) {
                                                        mStatus = "Student";
                                                    } else if (spinnerValueStatus == 2) {
                                                        mStatus = "Instructor";
                                                    }
                                                    Map<String, Object> userMap = new HashMap<>();
                                                    userMap.put("name", name);
                                                    userMap.put("email", email);
                                                    userMap.put("image", download_url);
                                                    userMap.put("token_id", token_id);
                                                    userMap.put("status", mStatus);

                                                    mFirestore.collection("Users").document(user_email).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            mRegisterProgressBar.setVisibility(View.INVISIBLE);

                                                            sendToMain();

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                            Toast.makeText(RegisterActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                            mRegisterProgressBar.setVisibility(View.INVISIBLE);

                                                        }
                                                    });

                                                }
                                                //Toast.makeText(MtActivity.this, "Upload Done", Toast.LENGTH_LONG).show();

                                            });

                                        } else {

                                            Toast.makeText(RegisterActivity.this, "Error : " + uploadTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            mRegisterProgressBar.setVisibility(View.INVISIBLE);

                                        }


                                    }
                                });

                                Toast.makeText(RegisterActivity.this, "Success! Account created ", Toast.LENGTH_LONG).show();


                            } else {

                                Toast.makeText(RegisterActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                mRegisterProgressBar.setVisibility(View.INVISIBLE);

                            }

                        }
                    });
                }else {
                        Toast.makeText(RegisterActivity.this, "Error : Please select if you are a student or not", Toast.LENGTH_LONG).show();
                        mRegisterProgressBar.setVisibility(View.INVISIBLE);
                    }

            }else
                Toast.makeText(RegisterActivity.this, "Error. Please fill in all fields",Toast.LENGTH_LONG).show();


        } else
            Toast.makeText(RegisterActivity.this, "Error. Please upload an image.",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE){

            imageUri = data.getData();
            mImageBtn.setImageURI(imageUri);

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
