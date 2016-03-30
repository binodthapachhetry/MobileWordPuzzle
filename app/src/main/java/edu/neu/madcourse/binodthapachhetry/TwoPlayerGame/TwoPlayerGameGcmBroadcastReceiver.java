package edu.neu.madcourse.binodthapachhetry.TwoPlayerGame;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by jarvis on 3/21/16.
 */
public class TwoPlayerGameGcmBroadcastReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "GcmBroadcaseReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "called");
        ComponentName comp = new ComponentName(context.getPackageName(), TwoPlayerGameGcmIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
//        setResultCode(Activity.RESULT_OK);
    }
}
