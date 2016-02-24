package edu.neu.madcourse.binodthapachhetry.Scraggle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import edu.neu.madcourse.binodthapachhetry.MainActivity;
import edu.neu.madcourse.binodthapachhetry.R;


public class ScraggleControlFragment extends Fragment {

    private AlertDialog mDialog;
    private MediaPlayer mMediaPlayer;
    public Context contextControl;
    public ScraggleGameActivityFragment mScraggleGame;
    public CountDownTimer timerPhase1;
    public CountDownTimer timerPhase2;


    onTimeEndListener timerListener;
    onRestartButtonClickListener restartListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_scraggle_control, container, false);

        final TextView phaseCounter = (TextView) rootView.findViewById(R.id.scragglephasecount);
       final TextView scraggleTimer = (TextView) rootView.findViewById(R.id.scraggletimer);
        View buttonPause = rootView.findViewById(R.id.scraggle_button_pause);
        View buttonResume = rootView.findViewById(R.id.scraggle_button_resume_game);
        View buttonRestart = rootView.findViewById(R.id.scraggle_button_restart_game);
        View buttonQuit = rootView.findViewById(R.id.scraggle_button_quit);

//        mMediaPlayer = MediaPlayer.create(this.getContext(),R.raw.updatedtimer);

        phaseCounter.setText("1");

        timerPhase1 = new CountDownTimer(90000,1000){
            MediaPlayer mMediaPlayer = MediaPlayer.create(getActivity(),R.raw.updatedtimer);
            @Override
            public void onTick(long millisUntilFinished) {

                scraggleTimer.setText(String.valueOf(millisUntilFinished/ 1000));
                scraggleTimer.setTextColor(Color.GREEN);

                if (millisUntilFinished/ 1000 < 30){
                    scraggleTimer.setTextColor(Color.RED);
                    // play timer

//                    mMediaPlayer.setLooping(true);

                    mMediaPlayer.start();

                }
            }
            @Override
            public void onFinish(){
                timerListener.changeToNextLevel();

                mMediaPlayer.stop();
                phaseCounter.setText("2");
                timerPhase2 = new CountDownTimer(90000,1000) {
                    MediaPlayer mMediaPlayer = MediaPlayer.create(getActivity(),R.raw.updatedtimer);
                    @Override
                    public void onTick(long millisUntilFinished) {
                        scraggleTimer.setText(String.valueOf(millisUntilFinished / 1000));
                        scraggleTimer.setTextColor(Color.GREEN);

                        if (millisUntilFinished / 1000 < 30) {
                            scraggleTimer.setTextColor(Color.RED);
                            // play timer

//                    mMediaPlayer.setLooping(true);
                            mMediaPlayer.start();

                        }
                    }

                    @Override
                    public void onFinish(){
                        mMediaPlayer.stop();
                    }

                }.start();

            }
        }.start();

//        phaseCounter.setText("2");
//
//        timerPhase2 = new CountDownTimer(90000,1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                scraggleTimer.setText(String.valueOf(millisUntilFinished / 1000));
//                scraggleTimer.setTextColor(Color.GREEN);
//
//                if (millisUntilFinished / 1000 < 30) {
//                    scraggleTimer.setTextColor(Color.RED);
//                    // play timer
//
////                    mMediaPlayer.setLooping(true);
//                    mMediaPlayer = MediaPlayer.create(getContext(), R.raw.updatedtimer);
//                    mMediaPlayer.start();
//
//                }
//            }
//
//            @Override
//            public void onFinish(){
//                    mMediaPlayer.stop();
//                }
//
//        }.start();


//        buttonPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((ScraggleGameActivity) getActivity()).onPause();
////                timer.start();
////                scraggleTimer.setTextSize(40);
//
//            }
//        });
//
//        buttonResume.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((ScraggleGameActivity) getActivity()).onResume();
////                timer.start();
////                scraggleTimer.setTextSize(40);
//
//            }
//        });

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerPhase1.cancel();
                ((ScraggleGameActivity) getActivity()).restartGame();
                phaseCounter.setText("1");

//                timerPhase2.cancel();
                timerPhase1.start();
                scraggleTimer.setTextSize(40);
                restartListener.clearScores();
                restartListener.clearWordList();

            }
        });

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        return rootView;

    }


    public void goBack(){
        Intent helpIntent = new Intent(getActivity(), ScraggleMainActivity.class);
        startActivity(helpIntent);

    }


    public interface onRestartButtonClickListener

    {
        public void clearScores();
        public void clearWordList();

    }
public interface onTimeEndListener
{
    public void changeToNextLevel();

}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            timerListener = (onTimeEndListener) activity;
            restartListener = (onRestartButtonClickListener) activity;

        } catch (Exception e){

        }



    }
}
