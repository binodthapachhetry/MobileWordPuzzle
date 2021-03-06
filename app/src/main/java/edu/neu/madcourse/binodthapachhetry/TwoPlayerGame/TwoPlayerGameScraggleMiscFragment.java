package edu.neu.madcourse.binodthapachhetry.TwoPlayerGame;

import android.app.Activity;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.neu.madcourse.binodthapachhetry.R;


public class TwoPlayerGameScraggleMiscFragment extends Fragment{
    public String wordsFromBoard;
    TextView wordLis;
    TextView scaggleScoreNum;
    TextView myName;
    TextView opponentName;
    TextView opponent_scaggleScoreNum;
    onTimeEndListener timeListener;

    private MediaPlayer mMediaPlayer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_twoplayergame_scraggle_misc, container, false);

        myName = (TextView) rootView.findViewById(R.id.two_player_game_scragglescore);
        if (TwoPlayerGameReturningUser.myName != null) {
            myName.setText(TwoPlayerGameReturningUser.myName);
        }else{
            myName.setText(TwoPlayerGameGcmIntentService.myNameFromGCM);
        }

        opponentName = (TextView) rootView.findViewById(R.id.two_player_game_opponent_scragglescore);
        if (TwoPlayerGameReturningUser.opponentName != null) {
            opponentName.setText(TwoPlayerGameReturningUser.opponentName);
        }else{
            opponentName.setText(TwoPlayerGameGcmIntentService.opponentNameFromGCM);
        }

        if (TwoPlayerGameGcmIntentService.opponentScoreFromGCM != null){

        }

        scaggleScoreNum = (TextView) rootView.findViewById(R.id.two_player_game_scragglescorenum);

        opponent_scaggleScoreNum = (TextView) rootView.findViewById(R.id.two_player_game_opponent_scragglescorenum);

        View buttonMute = rootView.findViewById(R.id.two_player_game_button_mute);
        wordLis = (TextView) rootView.findViewById(R.id.two_player_game_scraggleTextViewBox);

        buttonMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer = ((TwoPlayerGameScraggleGameActivity) getActivity()).mMediaPlayer;
                mMediaPlayer.stop();
            }
        });

        return rootView;
    }

    public void showWords(String s){
        wordLis.append(s + "\n");
    }

    public void showScores(int score){

        String scoreDisplay = String.valueOf(score);
        scaggleScoreNum.setText(scoreDisplay);

    }

    public void showOpponentScores(String score){
        opponent_scaggleScoreNum.setText(score);
    }

    public interface onRestartButtonClickListener
    {
        public void clearScores();
        public void clearWordList();
    }

    public void clearScores(){

        scaggleScoreNum.setText("0");
    }

    public void clearWordList(){
        wordLis.setText("");
    }

    //Interface class to pass values between two strings
    public interface onTimeEndListener
    {
        public void changeToNextLevel();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            timeListener = (onTimeEndListener) activity;
        } catch (Exception e){

        }

    }

}
