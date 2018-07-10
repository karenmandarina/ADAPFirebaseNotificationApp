package com.adaptwo.adap.firebasenotificationapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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


        String messageTitle = remoteMessage.getData().get("title");
        String messageBody = remoteMessage.getData().get("body");

        String click_action = remoteMessage.getData().get("click_action");
        String dataMessage = remoteMessage.getData().get("message");
        String dataFrom = remoteMessage.getData().get("from_user_id");
        Log.d("NotificationsApp", "we have the data from Firebase: " + dataFrom + dataMessage);
        //String launcher = remoteMessage.getData().get("launcherImage");

//        long[] dataPattern = remoteMessage.getData().get("pattern");

//        String dataVibTime = remoteMessage.getData().get("vibrationTime");
//        String dataVibAmp = remoteMessage.getData().get("vibrationAmp");



        //final long[] pattern = {0, 100, 200, 100, 200, 100}; // sleep for 200 milliseconds and vibrate for 100 milliseconds

        String channelId= getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                ;

        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("message", dataMessage);
        resultIntent.putExtra("from_user_id", dataFrom);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        Log.d("NotificationsApp", "pending intent" );



        int mNotificationId = (int) System.currentTimeMillis();
        Log.d("test", "onMessageReceived: mnotificationid is " + mNotificationId );

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId, "Channel readable title",notificationManager.IMPORTANCE_DEFAULT );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(mNotificationId, mBuilder.build());
        NotificationManagerCompat mNotifyMgr = NotificationManagerCompat.from(this);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

//
//        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(this);
//        notificationManager.notify(mNotificationId,mBuilder.build())

        Log.d("NotificationsApp", "Notified" );



    }


}