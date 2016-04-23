package edu.neu.madcourse.binodthapachhetry.FindingAstro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.neu.madcourse.binodthapachhetry.R;

public class FindingAstroMain extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "FindingAstroMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_astro_main);
    }

    @Override
    public void onClick(View view) {

        if (view == findViewById(R.id.finding_astro_skyview_button)){
            Intent myIntent = new Intent(getApplicationContext(),edu.neu.madcourse.binodthapachhetry.FindingAstro.FindingAstroReady.class);
            startActivity(myIntent);
        };


        if (view == findViewById(R.id.finding_astro_accel_button)){
            Intent myIntent = new Intent(getApplicationContext(),edu.neu.madcourse.binodthapachhetry.FindingAstro.FindingAstroAccelerometer.class);
            startActivity(myIntent);
        };


        if (view == findViewById(R.id.finding_astro_rotation_button)){
            Intent myIntent = new Intent(getApplicationContext(),edu.neu.madcourse.binodthapachhetry.FindingAstro.RotationVector.class);
            startActivity(myIntent);

        };

        if (view == findViewById(R.id.finding_astro_rules_button)){
            Intent myIntent = new Intent(getApplicationContext(),edu.neu.madcourse.binodthapachhetry.FindingAstro.Rules.class);
            startActivity(myIntent);

        };


    }

}
