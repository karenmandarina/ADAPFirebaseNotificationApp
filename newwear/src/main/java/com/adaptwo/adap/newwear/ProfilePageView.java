package com.adaptwo.adap.newwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by karenmandarina on 7/2/18.
 */

public class ProfilePageView extends Activity {
    private Button ProBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);

        ProBack = findViewById(R.id.profile_back);

        ProBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackMain = new Intent(ProfilePageView.this, MainActivity.class);
                startActivity(BackMain);
            }
        });
    }
}

