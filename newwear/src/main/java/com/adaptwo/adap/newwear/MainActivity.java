package com.adaptwo.adap.newwear;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Button send_tkn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send_tkn = findViewById(R.id.send_tkn);

        send_tkn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "Clicked");

//                mAuth = FirebaseAuth.getInstance();
//                mFirestore = FirebaseFirestore.getInstance();

                final String token_id = FirebaseInstanceId.getInstance().getToken();
                //String current_id = mAuth.getCurrentUser().getUid();

                Log.d("TAG", "Running");
                Log.d("TAG", "1 Token ID watch: " + token_id);
                //Log.d("TAG", "Current UID " + current_id);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, token_id);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);


//
//                Intent i;
//                PackageManager manager = getPackageManager();
//                try {
//                    i = manager.getLaunchIntentForPackage("com.adaptwo.adap.firebasenotificationapp");
//                    if (i == null)
//                        throw new PackageManager.NameNotFoundException();
//                    i.addCategory(Intent.CATEGORY_LAUNCHER);
//                    i.setAction("android.intent.action.MAIN");
//                    i.putExtra("Send", token_id );
//                    startActivity(i);
//                    Log.d("TAG", "Intent Sent");
//
//                } catch (PackageManager.NameNotFoundException e) {
//                    Log.d("TAG", "Error " + e);
//                }

            }
        });


//        // Upon logging in we get the Token ID of the devise the user is logging into
//        // this way we can send notification to that devise via the Token ID
//        Map<String, Object> watchtokenMap = new HashMap<>();
//        watchtokenMap.put("Watch_token_id", token_id);
//
//        mFirestore.collection("Users").document(current_id)
//                .update(watchtokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d("TAG", "Token ID watch: " + token_id);
//
//            }
//        });
    }
}
