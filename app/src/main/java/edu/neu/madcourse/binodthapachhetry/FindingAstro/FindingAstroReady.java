package edu.neu.madcourse.binodthapachhetry.FindingAstro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import edu.neu.madcourse.binodthapachhetry.R;

public class FindingAstroReady extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_astro_ready);
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.finding_astro_registered_user_button)) {
            Intent myIntent = new Intent(getApplicationContext(), edu.neu.madcourse.binodthapachhetry.FindingAstro.FindingAstroRegisteredUser.class);
            startActivity(myIntent);

        }

        if (view == findViewById(R.id.finding_astro_new_user_button)) {
            Intent myIntent = new Intent(getApplicationContext(), edu.neu.madcourse.binodthapachhetry.FindingAstro.FindingAstroNewUser.class);
            startActivity(myIntent);

        }
    }

}
