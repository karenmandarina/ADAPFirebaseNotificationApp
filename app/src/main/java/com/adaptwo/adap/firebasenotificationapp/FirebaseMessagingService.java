package com.adaptwo.adap.firebasenotificationapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.adaptwo.adap.firebasenotificationapp.R;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by KarenMandarina on 10/01/18.
 */

// to receive notification while app is running
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        int color;
        color = getColor(R.color.colorPrimaryDark);

        // Receiving data from cloud functions' data paylaod
        String messageTitle = remoteMessage.getData().get("title");
        String messageBody = remoteMessage.getData().get("body");
        String click_action = remoteMessage.getData().get("click_action");
        String dataMessage = remoteMessage.getData().get("message");
        String dataFrom = remoteMessage.getData().get("from_user_id");
        String dataPosVib = remoteMessage.getData().get("positiveVib");
        String dataCorVib = remoteMessage.getData().get("correctiveVib");

        Log.d("NotificationsApp", "we have the data from Firebase: " + dataFrom + dataMessage);
        Log.d("NotificationsApp", "positive num: " + dataPosVib);
        Log.d("NotificationsApp", "corrective num: " + dataCorVib);
        long[] pattern;

        if (dataPosVib.equals("0")) {
            pattern = new long[]{0, 100, 100, 100, 0, 0, 0};
            Log.d("NotificationsApp", "Pos default set");
        } else if (dataPosVib.equals("1")) {
            pattern = new long[]{0, 100, 100, 200, 100, 0, 0};
            Log.d("NotificationsApp", "Pos 1 set");
        } else if (dataPosVib.equals("2")) {
            pattern = new long[]{0, 100, 100, 200, 100, 300, 100};
            Log.d("NotificationsApp", "Pos 2 set");
        } else if (dataCorVib.equals("0")) {
            pattern = new long[]{0, 1000, 200, 0, 0, 0, 0};
            Log.d("NotificationsApp", "Cor default set");
        } else if (dataCorVib.equals("1")) {
            pattern = new long[]{0, 1000, 200, 100, 100, 0, 0};
            Log.d("NotificationsApp", "Cor 1 set");
        } else if (dataCorVib.equals("2")) {
            pattern = new long[]{0, 1000, 200, 100, 100, 100, 100};
            Log.d("NotificationsApp", "Cor 2 set");
        } else { // For custom messages
            pattern = new long[]{0, 500, 100, 0, 0, 0, 0};
            Log.d("NotificationsApp", "Custom set");
        }

            // Constructing a notification from the data received above
            String channelId = getString(R.string.default_notification_channel_id);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(messageTitle)
                            .setContentText(messageBody)
                            .setColor(color)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setAutoCancel(true)
                            .extend(new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true));

            // Intent for when the notification is clicked
            Intent resultIntent = new Intent(click_action);
            resultIntent.putExtra("message", dataMessage);
            resultIntent.putExtra("from_user_id", dataFrom);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);

            // Unique ID
            int mNotificationId = (int) System.currentTimeMillis();
            Log.d("test", "onMessageReceived: notificationid is " + mNotificationId);

            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE);

            // For newer SDK, a notification channel is needed
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        channelId, "Channel readable title", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true);
                channel.setVibrationPattern(pattern);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);

                Vibrator vibrator;
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                assert vibrator != null;
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));

            } else {
                mBuilder.setVibrate(pattern);
            }

            //      notificationManager.notify(mNotificationId, mBuilder.build());
            NotificationManagerCompat mNotifyMgr = NotificationManagerCompat.from(this);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());

            //       NotificationManagerCompat notificationManager= NotificationManagerCompat.from(this);
            //       notificationManager.notify(mNotificationId,mBuilder.build())

            Log.d("NotificationsApp", "Notified");

        }

    }

