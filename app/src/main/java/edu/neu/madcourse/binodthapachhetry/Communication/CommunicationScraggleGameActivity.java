package edu.neu.madcourse.binodthapachhetry.Communication;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.binodthapachhetry.R;

public class CommunicationScraggleGameActivity extends Activity implements CommunicationScraggleControlFragment.onTimeEndListener, CommunicationScraggleGameActivityFragment.wordFoundListener, CommunicationScraggleControlFragment.onRestartButtonClickListener{
    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    public MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private String regid;
    private String myName;

    public CommunicationScraggleGameActivityFragment mGameFragment;
    public CommunicationScraggleMiscFragment mGameMisc;
    public CommunicationScraggleControlFragment mGameControl;
    CommunicationMain mainCommunication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_scraggle_game);

        mGameFragment = (CommunicationScraggleGameActivityFragment) getFragmentManager().findFragmentById(R.id.scraggle_communication_fragment_game);
        mGameMisc = (CommunicationScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_communication_fragment_misc);
        mGameControl = (CommunicationScraggleControlFragment) getFragmentManager().findFragmentById(R.id.scraggle_communication_fragment_control);





        boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
        if (restore) {
            String gameData = getPreferences(MODE_PRIVATE)
                    .getString(PREF_RESTORE, null);
            if (gameData != null) {
                mGameFragment.putState(gameData);
            }
        }

    }

    public void addWords(String word) {

        CommunicationScraggleMiscFragment mGameMisc = (CommunicationScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_communication_fragment_misc);
        Log.d("Adding word: ", word);
        mGameMisc.showWords(word);

    }

    public void addScores(int score) {
        CommunicationScraggleMiscFragment mGameMisc = (CommunicationScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_communication_fragment_misc);
        Log.d("Score", Integer.toString(score));
        mGameMisc.showScores(score);
    }

    public void sendNotification(String word, Integer score){
        sendMessage(word,score);
    }



    public void changeToNextLevel() {
        CommunicationScraggleGameActivityFragment gameFragmentTimer = (CommunicationScraggleGameActivityFragment) getFragmentManager().findFragmentById(R.id.scraggle_communication_fragment_game);
        gameFragmentTimer.isLevelTwo();
    }


    public void restartGame() {
        mGameFragment.restartGame();
    }


    @Override
    protected void onResume() {
        super.onResume();
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
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(null);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        String gameData = mGameFragment.getState();
        getPreferences(MODE_PRIVATE).edit()
                .putString(PREF_RESTORE, gameData)
                .commit();
    }

    @Override
    public void clearScores() {
        CommunicationScraggleMiscFragment mGameMisc = (CommunicationScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_communication_fragment_misc);
        Log.d("Scores", String.valueOf(mGameMisc.scaggleScoreNum.getText()));
        mGameMisc.clearScores();

    }

    @Override
    public void clearWordList() {
        CommunicationScraggleMiscFragment mGameMisc = (CommunicationScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_communication_fragment_misc);
        mGameMisc.clearWordList();

    }

    public void sendMessage(final String word, final Integer score) {

        regid = CommunicationMain.regid;
        myName = CommunicationMain.myName;

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
                GcmNotification gcmNotification = new GcmNotification();
                regIds.clear();
                regIds.add(reg_device);
                gcmNotification.sendNotification(msgParams, regIds,CommunicationScraggleGameActivity.this);
                return "Message Sent - " + message;
            }

            @Override
            protected void onPostExecute(String msg) {
//                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }


}
