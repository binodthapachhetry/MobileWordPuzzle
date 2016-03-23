package edu.neu.madcourse.binodthapachhetry.Communication;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.neu.madcourse.binodthapachhetry.R;

public class CommunicationScraggleGameActivity extends Activity implements CommunicationScraggleControlFragment.onTimeEndListener, ScraggleGameActivityFragment.wordFoundListener, CommunicationScraggleControlFragment.onRestartButtonClickListener{
    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    public MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();

    public ScraggleGameActivityFragment mGameFragment;
    public ScraggleMiscFragment mGameMisc;
    public CommunicationScraggleControlFragment mGameControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scraggle_game);

        mGameFragment = (ScraggleGameActivityFragment) getFragmentManager().findFragmentById(R.id.scraggle_fragment_game);
        mGameMisc = (ScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_fragment_misc);
        mGameControl = (CommunicationScraggleControlFragment) getFragmentManager().findFragmentById(R.id.scraggle_fragment_control);





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

        ScraggleMiscFragment mGameMisc = (ScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_fragment_misc);
        mGameMisc.showWords(word);

    }

    public void addScores(int score) {
        ScraggleMiscFragment mGameMisc = (ScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_fragment_misc);
        Log.d("Score", Integer.toString(score));
        mGameMisc.showScores(score);
    }


    public void changeToNextLevel() {
        ScraggleGameActivityFragment gameFragmentTimer = (ScraggleGameActivityFragment) getFragmentManager().findFragmentById(R.id.scraggle_fragment_game);
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
        ScraggleMiscFragment mGameMisc = (ScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_fragment_misc);
        Log.d("Scores", String.valueOf(mGameMisc.scaggleScoreNum.getText()));
        mGameMisc.clearScores();

    }

    @Override
    public void clearWordList() {
        ScraggleMiscFragment mGameMisc = (ScraggleMiscFragment) getFragmentManager().findFragmentById(R.id.scraggle_fragment_misc);
        mGameMisc.clearWordList();

    }
}
