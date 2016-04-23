package edu.neu.madcourse.binodthapachhetry.FindingAstro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.binodthapachhetry.R;

public class FindingAstroDescription extends AppCompatActivity implements View.OnClickListener{
    Button inButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_astro_description);
        inButton = (Button) findViewById(R.id.finding_astro_in);
    }



    @Override
    public void onClick(View v) {
        Intent myIntent = new Intent(getApplicationContext(),edu.neu.madcourse.binodthapachhetry.FindingAstro.FindingAstroMain.class);
        startActivity(myIntent);

    }
}
