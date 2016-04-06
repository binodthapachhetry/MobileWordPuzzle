package edu.neu.madcourse.binodthapachhetry.TwoPlayerGame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.neu.madcourse.binodthapachhetry.Communication.CommunicationMain;
import edu.neu.madcourse.binodthapachhetry.R;


public class TwoPlayerGameScraggleMainActivity extends AppCompatActivity {
    private static final String TAG = "TPGMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twoplayergame_scraggle_main);

//        Intent service1Intent = new Intent(this, TrackingService.class);
//
//        service1Intent.putExtra("user_name", CommunicationMain.myName);
//        startService(service1Intent);



    }

}
