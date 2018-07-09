package com.adaptwo.adap.firebasenotificationapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(dataFrom)
                        .setContentText(dataMessage)
                ;
        Log.d("NotificationsApp", "we have the notification built" );




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
        Log.d("NotificationsApp", "pending intent" );



        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        Log.d("NotificationsApp", "Notified" );



    }


}
