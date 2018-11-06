package com.adaptwo.adap.firebasenotificationapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AkshayeJH on 04/01/18.
 */

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    private List<Users> usersList;
    private Context context;
    private FirebaseFirestore mFirestore;
    private String mUserId;
    private FirebaseAuth mAuth;
    private String mStatus;

    public UsersRecyclerAdapter(Context context, List<Users> usersList){

        this.usersList = usersList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent,
                false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final String user_name = usersList.get(position).getName();
        final String user_email = usersList.get(position).getEmail();

        holder.user_name_view.setText(user_name);

        CircleImageView user_image_view = holder.user_image_view;
        Glide.with(context).load(usersList.get(position).getImage()).into(user_image_view);

        final String user_id = usersList.get(position).userId;

        // Status has been restricted to be either 1 or 2. 1 is a student, 2 is instructor.
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUserId = mAuth.getCurrentUser().getEmail();
        // retreiving current user's status Firestore

        mFirestore.collection("Users").document(mUserId).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        mStatus = documentSnapshot.getString("status");
                        Log.d("NotificationsApp", "Users list got the status " + mStatus);

                        // Is the user is an instructor, then take them to the "Send Activity page"
                        // else (a student) don't do anything
                        if  (mStatus.equals("Instructor")){
                            Log.d("NotificationsApp", "Users list instructor loop is good");
                             holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent sendIntent = new Intent(context, com.adaptwo.adap.firebasenotificationapp.SendActivity.class);
                                    sendIntent.putExtra("user_id", user_id);
                                    sendIntent.putExtra("user_email", user_email);
                                    sendIntent.putExtra("user_name", user_name);
                                    context.startActivity(sendIntent);

                                }
                            });

                        }else {
                            Log.d("NotificationsApp", "Users list student loop is good");

                        }

                    }
                });



    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private CircleImageView user_image_view;
        private TextView user_name_view;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            user_image_view = (CircleImageView) mView.findViewById(R.id.user_list_image);
            user_name_view = (TextView) mView.findViewById(R.id.user_list_name);

        }
    }

}
