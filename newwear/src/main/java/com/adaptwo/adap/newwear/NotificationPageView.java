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

public class NotificationPageView extends Activity {

    private Button Back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_view);

        Back = findViewById(R.id.notif_back);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(NotificationPageView.this, MainActivity.class);
                startActivity(main);
            }
        });
    }
}
