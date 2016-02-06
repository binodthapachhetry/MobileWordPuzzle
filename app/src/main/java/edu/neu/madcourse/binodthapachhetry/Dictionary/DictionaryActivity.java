package edu.neu.madcourse.binodthapachhetry.Dictionary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.neu.madcourse.binodthapachhetry.R;

public class DictionaryActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

}
