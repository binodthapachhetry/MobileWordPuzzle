package edu.neu.madcourse.binodthapachhetry.FindingAstro;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import edu.neu.madcourse.binodthapachhetry.R;

public class Proximity extends AppCompatActivity implements SensorEventListener{

    private final static String TAG = "Proximity";
    private SensorManager mySensorManager;
    private Sensor myProximitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

//        ProximitySensor = (TextView)findViewById(R.id.proximitySensor);
//        ProximityMax = (TextView)findViewById(R.id.proximityMax);
//        ProximityReading = (TextView)findViewById(R.id.proximityReading);

        mySensorManager = (SensorManager)getSystemService(
                Context.SENSOR_SERVICE);
        myProximitySensor = mySensorManager.getDefaultSensor(
                Sensor.TYPE_PROXIMITY);

        Log.d(TAG, String.valueOf(myProximitySensor.getMaximumRange()));


    }
    protected void onResume() {
        super.onResume();
        mySensorManager.registerListener(this, myProximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mySensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            Log.d(TAG,"Value changed to: " +String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
