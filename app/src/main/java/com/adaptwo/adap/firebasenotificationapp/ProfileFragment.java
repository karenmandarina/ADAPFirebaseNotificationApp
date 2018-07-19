package com.adaptwo.adap.firebasenotificationapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {

    private Button mLogoutBtn;
    private FirebaseAuth mAuth;
    private CircleImageView mProfileImage;
    private TextView mProfileName;
    private Button mHelp;

    private FirebaseFirestore mFirestore;
    private String mUserId;
    static final String FROM_PREFS = "EmailPrefs";
    static final String FROM_NAME = "senderEmail";

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        // getting data stored about the current user to display on "profile" page
        mUserId = mAuth.getCurrentUser().getUid();

        mLogoutBtn = (Button) view.findViewById(R.id.logout_btn);
        mProfileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        mProfileName = (TextView) view.findViewById(R.id.profile_name);
        mHelp = (Button) view.findViewById(R.id.help);

        // retreiving current user's name and image from Firestore
        mFirestore.collection("Users").document(mUserId).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String user_name = documentSnapshot.getString("name");
                String user_image = documentSnapshot.getString("image");

                // display user name in the TextView
                mProfileName.setText(user_name);

                // default image for profile pic
                RequestOptions placeholderOption = new RequestOptions();
                placeholderOption.placeholder(R.mipmap.default_image);

                // display user image in the CircleImageView
                Glide.with(container.getContext()).setDefaultRequestOptions(placeholderOption).
                        load(user_image).into(mProfileImage);
                //Log.d("NotificationApp", "User image: " + user_image);

//                Intent sendIntent = new Intent(getContext(),
//                  com.adaptwo.adap.firebasenotificationapp.SendActivity.class);
//                sendIntent.putExtra("from_name", user_name);
//                getContext().startActivity(sendIntent);



            }
        });


        mHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tut = new Intent(container.getContext(), Tutorial.class);
                getActivity().finish();
                startActivity(tut);
                Log.d("NotificationApp", "Help clicked ");

            }
        });



        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // delete the Token ID from firebase- so don't remember the devise the user logged into
                // This way if the user logs into a different devise a new Tken ID can be generated upon login
                Map<String, Object> tokenMapRemove = new HashMap<>();
                tokenMapRemove.put("token_id", FieldValue.delete());

                mFirestore.collection("Users").document(mUserId).update(tokenMapRemove).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        mAuth.signOut();
                        Intent loginIntent = new Intent(container.getContext(), LoginActivity.class);
                        getActivity().finish();
                        startActivity(loginIntent);
                        Log.d("NotificationApp", "Logged out");


                    }
                });

            }
        });

        return view;

    }

}

