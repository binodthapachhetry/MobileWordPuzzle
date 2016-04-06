package edu.neu.madcourse.binodthapachhetry.TwoPlayerGame;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.neu.madcourse.binodthapachhetry.R;

public class TwoPlayerGameStart extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "TwoPlayerGameStart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player_game_start);


    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.two_player_game_registered_user_button)) {
            Log.d(TAG, "clicked registered user button");
            Intent myIntent = new Intent(getApplicationContext(),edu.neu.madcourse.binodthapachhetry.TwoPlayerGame.TwoPlayerGameReturningUser.class);
            startActivity(myIntent);

        }

        if (view == findViewById(R.id.two_player_game_new_user_button)) {
            Log.d(TAG, "clicked new user button");
            Intent myIntent = new Intent(getApplicationContext(),edu.neu.madcourse.binodthapachhetry.TwoPlayerGame.TwoPlayerGameRegisterUser.class);
            startActivity(myIntent);

        }


    }
}
