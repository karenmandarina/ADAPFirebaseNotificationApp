package com.adaptwo.adap.newwear;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by karenmandarina on 7/2/18.
 */

public class NotificationPageView extends BroadcastReceiver {
    public static final String CONTENT_KEY = "contentText";
    final long[] pattern = {200, 100, 200, 100, 200, 100};
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent displayIntent = new Intent(context, MainActivity.class);
        String text = intent.getStringExtra(CONTENT_KEY);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(text)
                .extend(new Notification.WearableExtender()
                        .setDisplayIntent(PendingIntent.getActivity(context, 0, displayIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT)))

                .build();

        Vibrator vibrator  = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));

        }else {
            vibrator.vibrate(500);
        }
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
        Toast.makeText(context, context.getString(R.string.notification_posted), Toast.LENGTH_SHORT).show();
    }
}
