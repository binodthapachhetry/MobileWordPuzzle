package edu.neu.madcourse.binodthapachhetry.TwoPlayerGame;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.binodthapachhetry.R;

public class TwoPlayerGameScraggleGameActivity extends Activity implements TwoPlayerGameScraggleControlFragment.onTimeEndListener, TwoPlayerGameScraggleGameActivityFragment.wordFoundListener, TwoPlayerGameScraggleControlFragment.onRestartButtonClickListener{
    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    static final String SOME_ACTION = "com.google.android.c2dm.intent.RECEIVE";
    private static final String TAG = "GameActivity";

    public static String messageToSend = "";

    public MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private String regid;
    private String myName;
    Context context;

    public TwoPlayerGameScraggleGameActivityFragment mGameFragment;
    public TwoPlayerGameScraggleMiscFragment mGameMisc;
    public TwoPlayerGameScraggleControlFragment mGameControl;
    TwoPlayerGameMain mainTwoPlayerGame;
    public messageReceiver mMessageReceiver;
    public IntentFilter filter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.registerReceiver(mMessageReceiver, new IntentFilter(SOME_ACTION));
        setContentView(R.layout.activity_TwoPlayerGame_scraggle_game);

        mGameFragment = (TwoPlayerGameScraggleGameActivityFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_game);
        mGameMisc = (TwoPlayerGameScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_misc);
        mGameControl = (TwoPlayerGameScraggleControlFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_control);

        boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
        if (restore) {
            String gameData = getPreferences(MODE_PRIVATE)
                    .getString(PREF_RESTORE, null);
            if (gameData != null) {
                mGameFragment.putState(gameData);
            }
        }

        mMessageReceiver =new messageReceiver();

        filter = new IntentFilter(SOME_ACTION);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
//        Intent i= new Intent(this,GcmIntentService.class);
//        startService(i);

//        mMessageReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //do something based on the intent's action
//            }
//        };
//        registerReceiver(mMessageReceiver, filter);

    }

    public void addWords(String word) {

        TwoPlayerGameScraggleMiscFragment mGameMisc = (TwoPlayerGameScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_misc);
        Log.d("Adding word: ", word);
        mGameMisc.showWords(word);

    }

    public void addScores(int score) {
        TwoPlayerGameScraggleMiscFragment mGameMisc = (TwoPlayerGameScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_misc);
        Log.d("Score", Integer.toString(score));
        mGameMisc.showScores(score);
    }

    public void sendNotification(String word, Integer score){
        sendMessage(word, score);
    }



    public void changeToNextLevel() {
        TwoPlayerGameScraggleGameActivityFragment gameFragmentTimer = (TwoPlayerGameScraggleGameActivityFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_game);
        gameFragmentTimer.isLevelTwo();
    }


    public void restartGame() {
        mGameFragment.restartGame();
    }


    @Override
    protected void onResume() {

        super.onResume();
        Log.d(TAG, "on resume");
        this.registerReceiver(mMessageReceiver, filter);

        mMediaPlayer = MediaPlayer.create(this, R.raw.loop);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        String gameData = getPreferences(MODE_PRIVATE)
                .getString(PREF_RESTORE, null);
        if (gameData != null) {
            mGameFragment.putState(gameData);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "on start");
        this.registerReceiver(mMessageReceiver, filter);
    }



    @Override
    protected void onPause() {

        mHandler.removeCallbacks(null);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        String gameData = mGameFragment.getState();
        getPreferences(MODE_PRIVATE).edit()
                .putString(PREF_RESTORE, gameData)
                .commit();
        this.unregisterReceiver(mMessageReceiver);
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mMessageReceiver);
    }


    @Override
    public void clearScores() {
        TwoPlayerGameScraggleMiscFragment mGameMisc = (TwoPlayerGameScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_misc);
        Log.d("Scores", String.valueOf(mGameMisc.scaggleScoreNum.getText()));
        mGameMisc.clearScores();

    }

    @Override
    public void clearWordList() {
        TwoPlayerGameScraggleMiscFragment mGameMisc = (TwoPlayerGameScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_misc);
        mGameMisc.clearWordList();

    }

    public void sendMessage(final String message, final Integer score) {

        regid = TwoPlayerGameMain.regid;
        myName = TwoPlayerGameMain.myName;


//        StringBuilder builder = new StringBuilder();
//        builder.append(myName);
//        builder.append(" just found ");
//        builder.append(word);
//        builder.append(" .Total point so far is ");
//        builder.append(String.valueOf(score));
//        final String message = builder.toString();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                List<String> regIds = new ArrayList<String>();
                String reg_device = regid;
                Map<String, String> msgParams;
                msgParams = new HashMap<>();
                msgParams.put("data.message", message);
                GcmNotification gcmNotification = new GcmNotification();
                regIds.clear();
                regIds.add(reg_device);
                gcmNotification.sendNotification(msgParams, regIds,TwoPlayerGameScraggleGameActivity.this);
                return "Message Sent - " + message;
            }

            @Override
            protected void onPostExecute(String msg) {
//                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }

    public class messageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            String message = intent.getStringExtra("message");

            if(message != null && message != messageToSend) {
                TwoPlayerGameScraggleMiscFragment mGameMisc = (TwoPlayerGameScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_misc);
                Log.d("On receive : ", message);
                mGameMisc.showOpponentScores(message);
                messageToSend = message;
            }

            //do other stuff here
        }
    };


}
