package com.adaptwo.adap.newwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by karenmandarina on 7/5/18.
 */

public class MyStubBroadcastActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent();
        i.setAction("com.adapplus.adap.SHOW_NOTIFICATION");
        i.putExtra(NotificationPageView.CONTENT_KEY, getString(R.string.title));
        sendBroadcast(i);
        finish();
    }
}
