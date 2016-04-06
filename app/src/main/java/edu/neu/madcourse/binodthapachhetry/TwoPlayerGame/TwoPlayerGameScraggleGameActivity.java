package edu.neu.madcourse.binodthapachhetry.TwoPlayerGame;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

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
    private final static String HIGH_SCORE = "highScore";

    public static boolean GameRunning;
    private static boolean activityVisible;
    public static String messageToSend = "";

    public MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private String regid;
    private String yourRegid;
    private String myName;
    private String opponentName;
    Context context;

    public TwoPlayerGameScraggleGameActivityFragment mGameFragment;
    public TwoPlayerGameScraggleMiscFragment mGameMisc;
    public TwoPlayerGameScraggleControlFragment mGameControl;
    TwoPlayerGameMain mainTwoPlayerGame;
    TwoPlayerGameRemoteClient remoteClient;
    public IntentFilter filter;
    public Integer highScore;
    private Integer dbHighScore;
    Intent intent;
    Map<String, Object> updates;
    private static HashMap<String, HashMap<String, Object>> hMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameRunning = true;
        activityVisible = true;
        setContentView(R.layout.activity_twoplayergame_scraggle_game);
        updates = new HashMap<String, Object>();
        myName = TwoPlayerGameReturningUser.myName;
        if (myName == null){
            myName = TwoPlayerGameGcmIntentService.myNameFromGCM;
        }
//         Log.d(TAG,myName);

        remoteClient = new TwoPlayerGameRemoteClient(this);
        hMap = remoteClient.getHash();



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

//        mMessageReceiver =new messageReceiver();
//
//        filter = new IntentFilter(SOME_ACTION);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
//        Intent i= new Intent(this,TwoPlayerGameGcmIntentService.class);
//        startService(i);

//        mMessageReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //do something based on the intent's action
//            }
//        };
//        registerReceiver(mMessageReceiver, filter);

    }

    public void showOppScores(String score){
        mGameMisc.showOpponentScores(score);
    }

    public void addWords(String word) {

        TwoPlayerGameScraggleMiscFragment mGameMisc = (TwoPlayerGameScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_misc);
        Log.d("Adding word: ", word);
        mGameMisc.showWords(word);

    }

    public void addScores(int score) {
        TwoPlayerGameScraggleMiscFragment mGameMisc = (TwoPlayerGameScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_misc);
        Log.d("Score", Integer.toString(score));
        highScore = score;
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
        GameRunning = true;
        activityVisible = true;
        Log.d(TAG, "on resume");



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
        GameRunning = false;
        activityVisible = true;
        Log.d(TAG, "on start");
//        this.registerReceiver(mMessageReceiver, filter);
    }



    @Override
    protected void onPause() {
        GameRunning = false;
        activityVisible = false;

        updates.put(HIGH_SCORE, highScore);
        remoteClient.saveValue(myName, updates);

        mHandler.removeCallbacks(null);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        String gameData = mGameFragment.getState();
        getPreferences(MODE_PRIVATE).edit()
                .putString(PREF_RESTORE, gameData)
                .commit();
//        this.unregisterReceiver(mMessageReceiver);
        super.onPause();

    }

    @Override
    protected void onStop() {
        Log.d(TAG,"On Stop");
        GameRunning = false;
        activityVisible = false;

        Log.d(TAG,String.valueOf(hMap));

        HashMap<String, Object> temporaryHmap = hMap.get(myName);

        Log.d(TAG,String.valueOf(temporaryHmap));

        if (temporaryHmap.get(HIGH_SCORE) != null){
            dbHighScore = (int) (long) temporaryHmap.get(HIGH_SCORE);
            Log.d(TAG,String.valueOf(dbHighScore));
            if (highScore != null){
                if (highScore > dbHighScore){
                    updates.put(HIGH_SCORE, highScore);
                    remoteClient.saveValue(myName, updates);
                }
            }

        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"On Destroy");
        GameRunning = false;
        activityVisible = false;

        Log.d(TAG,String.valueOf(hMap));
        HashMap<String, Object> temporaryHmap = hMap.get(myName);
        Log.d(TAG,String.valueOf(temporaryHmap));

        if (temporaryHmap.get(HIGH_SCORE) != null){
            dbHighScore = (int) (long) temporaryHmap.get(HIGH_SCORE);
            Log.d(TAG,String.valueOf(dbHighScore));
            if (highScore != null){
                if (highScore > dbHighScore){
                    updates.put(HIGH_SCORE, highScore);
                    remoteClient.saveValue(myName, updates);
                }
            }

        }

        super.onDestroy();
    }

//    private Integer getScores(){
//        TwoPlayerGameScraggleMiscFragment mGameMisc = (TwoPlayerGameScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_misc);
//        Integer highScore = (Integer) mGameMisc.scaggleScoreNum.getText();
//        return highScore;
//
//    }


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

    private void sendMessage(final String word, final Integer score) {

        if (TwoPlayerGameReturningUser.regid != null){
            regid = TwoPlayerGameReturningUser.regid;
        }else{
            regid = TwoPlayerGameGcmIntentService.myRegIdFromGCM;
        }

        if (TwoPlayerGameReturningUser.yourRegID != null){
            yourRegid = TwoPlayerGameReturningUser.yourRegID;
        }else{
            yourRegid = TwoPlayerGameGcmIntentService.opponentRegIDfromGCM;
        }

        if (TwoPlayerGameReturningUser.myName != null) {
            myName = TwoPlayerGameReturningUser.myName;
        }else{
            myName = TwoPlayerGameGcmIntentService.myNameFromGCM;
        }

        Log.d(TAG,myName);
        Log.d(TAG,regid);
        if (TwoPlayerGameReturningUser.opponentName != null) {
            opponentName = TwoPlayerGameReturningUser.opponentName;
        }else{
            opponentName = TwoPlayerGameGcmIntentService.opponentNameFromGCM;
        }


        StringBuilder builder = new StringBuilder();
        builder.append(myName);
        builder.append(" just found ");
        builder.append(word);
        builder.append(" .Total point so far is ");
        builder.append(String.valueOf(score));
        final String message = builder.toString();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                List<String> regIds = new ArrayList<String>();
                String reg_device = regid;
                Map<String, String> msgParams;
                msgParams = new HashMap<>();
                msgParams.put("data.message", message);
                msgParams.put("data.myName",myName);
                msgParams.put("data.opponentName", opponentName);
                msgParams.put("data.myScore", String.valueOf(score));
                msgParams.put("data.myRegID",regid);
                msgParams.put("data.opponentRegId",yourRegid);
                Log.d(TAG,myName+ " " + regid);
                Log.d(TAG, opponentName + " " + yourRegid);
                TwoPlayerGameGcmNotification twoPlayerGameGcmNotification = new TwoPlayerGameGcmNotification();
                regIds.clear();
                regIds.add(reg_device);
                twoPlayerGameGcmNotification.sendNotification(msgParams, regIds,TwoPlayerGameScraggleGameActivity.this);
                return "Message Sent - " + message;
            }

            @Override
            protected void onPostExecute(String msg) {
//                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

//    public class messageReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            // Extract data included in the Intent
//            String message = intent.getStringExtra("myScore");
//
//            if(message != null) {
//                TwoPlayerGameScraggleMiscFragment mGameMisc = (TwoPlayerGameScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_TwoPlayerGame_fragment_misc);
//                Log.d(TAG, "UPDATING SCORE " + message);
//                mGameMisc.showOpponentScores(message);
//            }
//
//        }
//    };


}
