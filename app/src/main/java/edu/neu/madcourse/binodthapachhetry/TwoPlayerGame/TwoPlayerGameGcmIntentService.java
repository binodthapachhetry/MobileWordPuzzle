package edu.neu.madcourse.binodthapachhetry.TwoPlayerGame;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import edu.neu.madcourse.binodthapachhetry.R;


/**
 * Created by jarvis on 3/21/16.
 */
public class TwoPlayerGameGcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    static final String SOME_ACTION = "com.google.android.c2dm.intent.RECEIVE";
    private NotificationManager mNotificationManager;
    private static final String TAG = "TwoPlayerGameGcmIntent";

    public static String myNameFromGCM;
    public static String opponentNameFromGCM;
    public static String opponentScoreFromGCM;
    public static String opponentRegIDfromGCM;
    public static String myRegIdFromGCM;


    public TwoPlayerGameGcmIntentService() {
        super("TwoPlayerGameGcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        Log.d(TAG, extras.toString());
        if (!extras.isEmpty()) {

            opponentNameFromGCM = extras.getString("myName");
            if (opponentNameFromGCM !=null) {
                myNameFromGCM = extras.getString("opponentName");
                opponentScoreFromGCM = extras.getString("myScore");
                opponentRegIDfromGCM = extras.getString("myRegID");
                myRegIdFromGCM = extras.getString("opponentRegId");
            }

            String message = extras.getString("message");
            if (message != null) {
                sendNotification(message);
//                if (TwoPlayerGameScraggleGameActivity.isActivityVisible()) {
//                    Log.d(TAG, " it should not be sending notification");
//                    if(opponentScoreFromGCM !=null){
//                        updateMyActivity(getApplicationContext(), message);
//                    }
//
//
////                updateMyActivity(getApplicationContext(), message);
////                    sendNotification(message);
//                }else{
//                    sendNotification(message);
//                }
            }

//            opponentNameFromGCM = extras.getString("myName");
//            if (opponentNameFromGCM !=null){
//
//                myNameFromGCM = extras.getString("opponentName");
//                opponentScoreFromGCM = extras.getString("myScore");
//                opponentRegIDfromGCM = extras.getString("myRegID");
//                myRegIdFromGCM = extras.getString("opponentRegId");
//
//                Log.d(TAG,"My name, opponent name, opponent score : " + opponentNameFromGCM + "," + myNameFromGCM + "," + opponentScoreFromGCM );
//            }

        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        TwoPlayerGameGcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    public void sendNotification(String message) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent;
        notificationIntent = new Intent(this,TwoPlayerGameReturningUser.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("show_response", "show_response");
        Log.d(TAG,"PendingIntent to Returning user class");
        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this, edu.neu.madcourse.binodthapachhetry.TwoPlayerGame.TwoPlayerGameScraggleGameActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Message from Scraggle")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message).setTicker(message)
                .setAutoCancel(true);
        mBuilder.setContentIntent(intent);

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

//    static void updateMyActivity(Context context, String message) {
//
//        Intent intent = new Intent(SOME_ACTION);
//
//        //put whatever data you want to send, if any
//        intent.putExtra("message", message);
//
//        //send broadcast
//        context.sendBroadcast(intent);
//    }

}
