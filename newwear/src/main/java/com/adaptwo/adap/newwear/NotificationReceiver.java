package com.adaptwo.adap.newwear;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by karenmandarina on 7/2/18.
 */

public class NotificationReceiver extends BroadcastReceiver {
    public static final String CONTENT_KEY = "contentText";
    final long[] pattern = {200, 100, 200, 100, 200, 100};
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "On receive of notification; success ", Toast.LENGTH_SHORT).show();

        Intent displayIntent = new Intent(context, MainActivity.class);
        String text = intent.getStringExtra(CONTENT_KEY);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(text)
                .setContentText(text)
                .setLights(Color.RED, 1000, 1000)
                .setVibrate(new long[]{0, 400, 250, 400})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));

        // You must set the Notification Channel ID
        // if your app is targeting API Level 26 and up (Android O)
        // More info: http://bit.ly/2Bzgwl7
        builder.setChannelId("@string/default_notification_channel_id");

        // Get an instance of the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        // Build the notification and display it
        notificationManager.notify(1, builder.build());


//        Vibrator vibrator  = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        long[] vibrationPattern = {0, 500, 50, 300};
//        //-1 - don't repeat
//        final int indexInPatternToRepeat = -1;
//        vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);
        Toast.makeText(context, context.getString(R.string.notification_posted), Toast.LENGTH_SHORT).show();
    }
}
