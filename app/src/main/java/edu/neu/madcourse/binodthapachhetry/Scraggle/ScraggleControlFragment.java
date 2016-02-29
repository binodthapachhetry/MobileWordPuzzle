package edu.neu.madcourse.binodthapachhetry.Scraggle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
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
//    private MediaPlayer mMediaPlayer;
    public Context contextControl;
    public ScraggleGameActivityFragment mScraggleGame;
    private final long startTime = 90 * 1000;
    private final long interval = 1 * 1000;
    public CountDownTimer countDownTimerOne;
    public CountDownTimer countDownTimerTwo;
    private boolean timerStarted = false;
    private boolean isPaused = false;
    private long timeRemaining = 0;
    public FragmentManager fm;

    public TextView scraggleTimer;
    public TextView phaseCounter;


    onTimeEndListener timerListener;
    onRestartButtonClickListener restartListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_scraggle_control, container, false);

        phaseCounter = (TextView) rootView.findViewById(R.id.scragglephasecount);
       scraggleTimer = (TextView) rootView.findViewById(R.id.scraggletimer);
        final View buttonPause = rootView.findViewById(R.id.scraggle_button_pause);
        final View buttonResume = rootView.findViewById(R.id.scraggle_button_resume_game);
        final View buttonRestart = rootView.findViewById(R.id.scraggle_button_restart_game);
        View buttonQuit = rootView.findViewById(R.id.scraggle_button_quit);

        fm = getFragmentManager();

        buttonResume.setEnabled(false);


        phaseCounter.setText("1");
        countDownTimerOne = new MyCountDownTimerOne(startTime, interval);

        countDownTimerOne.start();

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (countDownTimerOne != null) {
                    countDownTimerOne.cancel();
                }

                if (countDownTimerTwo != null) {
                    countDownTimerTwo.cancel();
                }
                scraggleTimer.setTextSize(40);
                restartListener.clearScores();
                restartListener.clearWordList();
                phaseCounter.setText("1");

                ((ScraggleGameActivity) getActivity()).restartGame();
                countDownTimerOne = new MyCountDownTimerOne(startTime, interval);

                countDownTimerOne.start();

            }


        });

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Intent myIntent = new Intent(getActivity(), edu.neu.madcourse.binodthapachhetry.Scraggle.ScraggleMainActivity.class);

//                Intent helpIntent = new Intent(getActivity(), edu.neu.madcourse.binodthapachhetry.MainActivity.class);
                startActivity(myIntent);
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPaused = true;
                //Enable the resume and cancel button
                buttonResume.setEnabled(true);
                buttonPause.setEnabled(false);
                buttonRestart.setEnabled(false);


                Fragment f = fm.findFragmentById(R.id.scraggle_fragment_game);
                fm.beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .hide(f)
                        .commit();

            }
        });

        buttonResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPaused = false;

                if (countDownTimerOne != null) {
                    countDownTimerOne.cancel();
                    countDownTimerOne = new MyCountDownTimerOne(timeRemaining, interval);
                    countDownTimerOne.start();

                }else if (countDownTimerTwo != null) {
                    countDownTimerTwo.cancel();
                    countDownTimerTwo = new MyCountDownTimerOne(timeRemaining, interval);
                    countDownTimerTwo.start();

                }

                Fragment f = fm.findFragmentById(R.id.scraggle_fragment_game);
                fm.beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .show(f)
                        .commit();

                buttonPause.setEnabled(true);
                buttonResume.setEnabled(false);
                buttonRestart.setEnabled(true);

            }
        });


        return rootView;

    }


    public void goBack(){
        Intent helpIntent = new Intent(getActivity(), ScraggleMainActivity.class);
        startActivity(helpIntent);

    }


    public class MyCountDownTimerOne extends CountDownTimer {
        private MediaPlayer mPlayer;
        public MyCountDownTimerOne(long startTime, long interval) {
            super(startTime, interval);
            mPlayer = MediaPlayer.create(getActivity(),R.raw.updatedtimer);
        }

        @Override
        public void onFinish() {
            mPlayer.pause();
            Toast.makeText(getActivity(), "Entering Phase 2 !",
                    Toast.LENGTH_LONG).show();
            timerListener.changeToNextLevel();
            phaseCounter.setText("2");
            countDownTimerTwo = new MyCountDownTimerTwo(startTime, interval);
            countDownTimerTwo.start();


        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(isPaused){
                mPlayer.pause();
                cancel();
            }
            else {
                scraggleTimer.setText(String.valueOf(millisUntilFinished / 1000));
                scraggleTimer.setTextColor(Color.GREEN);
                timeRemaining = millisUntilFinished;

                if (millisUntilFinished / 1000 < 30) {
                    scraggleTimer.setTextColor(Color.RED);
                    mPlayer.start();
                }
            }
        }
    }

    public class MyCountDownTimerTwo extends CountDownTimer {
        private MediaPlayer mPlayerTwo;
        public MyCountDownTimerTwo(long startTime, long interval) {
            super(startTime, interval);
            mPlayerTwo = MediaPlayer.create(getActivity(),R.raw.updatedtimer);
        }

        @Override
        public void onFinish() {
            mPlayerTwo.stop();
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.quit_text);

            builder.setPositiveButton(R.string.scraggle_quit_game,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface,
                                            int i) {

                            Intent helpIntent = new Intent(getActivity(), edu.neu.madcourse.binodthapachhetry.MainActivity.class);
                            startActivity(helpIntent);

                        }
                    });

            builder.setNegativeButton(R.string.scraggle_game_restart,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface,
                                            int i) {
                            scraggleTimer.setTextSize(40);
                            restartListener.clearScores();
                            restartListener.clearWordList();
                            phaseCounter.setText("1");

                            ((ScraggleGameActivity) getActivity()).restartGame();
                            countDownTimerOne = new MyCountDownTimerOne(startTime, interval);

                            countDownTimerOne.start();


                        }
                    });
            mDialog = builder.show();


        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(isPaused){
                mPlayerTwo.pause();
                cancel();
            }
            else {
                timeRemaining = millisUntilFinished;
                scraggleTimer.setText(String.valueOf(millisUntilFinished / 1000));
                scraggleTimer.setTextColor(Color.GREEN);

                if (millisUntilFinished / 1000 < 30) {
                    scraggleTimer.setTextColor(Color.RED);
                    mPlayerTwo.start();
                }
            }
        }
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
