package edu.neu.madcourse.binodthapachhetry.FindingAstro;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.binodthapachhetry.R;

public class FindingAstroAccelerometer extends Activity implements
        SensorEventListener {

    protected LowPassFilterSmoothing lpfAccelSmoothing;
    protected SensorManager sensorManager;
    protected Sensor accelerometer;
    protected Handler handler;

    protected Runnable runable;

    protected volatile float[] linearAcceleration = new float[3];

    TextView quadOne;
    TextView quadTwo;
    TextView quadThree;
    TextView quadFour;


    float[] listX = new float[15];
    float[] listY = new float[15];
    Integer count = 0;
    List<Double> accelerXdir = new ArrayList<Double>();
    List<Double> accelerYdir = new ArrayList<Double>();


    double x_y_degree;
    double accelerXprev;
    double accelerYprev;
    boolean firstTimeOnSensorChange = true;

    String quadrant = null;


    static final float ALPHA = 0.25f; // if ALPHA = 1 OR 0, no filter applies.

    Timer timer;

//    private AccelerationVectorView view;

    private static final String TAG = "FindAstroAccelerometer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_astro_accelerometer);

        quadOne = (TextView) findViewById(R.id.accelerometer_quadrant_one);
        quadTwo = (TextView) findViewById(R.id.accelerometer_quadrant_two);
        quadThree = (TextView) findViewById(R.id.accelerometer_quadrant_three);
        quadFour = (TextView) findViewById(R.id.accelerometer_quadrant_four);


        lpfAccelSmoothing = new LowPassFilterSmoothing();

        sensorManager = (SensorManager) this
                .getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
        {
            // Get a local copy of the sensor values
            System.arraycopy(event.values, 0, linearAcceleration, 0,
                    event.values.length);
            float[] acceler = new float[] {linearAcceleration[0],linearAcceleration[1],linearAcceleration[2]};
            acceler = lpfAccelSmoothing.addSamples(acceler);
            double accelerX = acceler[0];
            double accelerY = acceler[1];

//            accelerXdir.add(accelerX);
//            accelerYdir.add(accelerY);
//            if(accelerXdir.size()>1){
//
//                double accelerXprev = accelerXdir.get(accelerXdir.size()-2);
//                double accelerYprev = accelerYdir.get(accelerYdir.size()-2);
//
////                Log.d(TAG, String.valueOf(accelerX * accelerXprev) + "," + String.valueOf());
//                if(accelerX * accelerXprev < 0 || accelerY * accelerYprev < 0){
//                    Log.d(TAG, "It's decelerating!!");
//                    accelerXdir.clear();
//                    accelerYdir.clear();
//
//                    try {
//                        Thread.sleep(3000);                 //1000 milliseconds is one second.
//                    } catch(InterruptedException ex) {
//                        Thread.currentThread().interrupt();
//                    }
//                }else{
                    double mag = Math.sqrt(accelerX * accelerX + accelerY * accelerY);
                    x_y_degree = Math.toDegrees(Math.atan2(accelerY, accelerX));
                    if (mag > 2) {
                        String text = "Mag:" + String.valueOf(mag) + " Degree:" + String.valueOf(x_y_degree);
                        if (accelerX > 0 && accelerY > 0) {
                            quadFour.setText("");
                            quadThree.setText("");
                            quadTwo.setText("");
                            quadOne.setText(text);
                            quadrant = "one";

                            Log.d(TAG, "X : " + String.valueOf(accelerX) + " Y: " + String.valueOf(accelerY));

                            Log.d(TAG, "First quadrant! " + String.valueOf(mag) + " " + String.valueOf(x_y_degree));
                        }

                        if (accelerX < 0 && accelerY > 0) {
                            quadFour.setText("");
                            quadOne.setText("");
                            quadTwo.setText("");
                            quadThree.setText(text);
                            quadrant = "three";
                            Log.d(TAG, "X : " + String.valueOf(accelerX) + " Y: " + String.valueOf(accelerY));


                            Log.d(TAG, "Third quadrant! " + String.valueOf(mag) + " " + String.valueOf(x_y_degree));
                        }
                        if (accelerX > 0 && accelerY < 0) {
                            quadOne.setText("");
                            quadFour.setText("");
                            quadThree.setText("");
                            quadTwo.setText(text);
                            quadrant = "two";
                            Log.d(TAG, "X : " + String.valueOf(accelerX) + " Y: " + String.valueOf(accelerY));

                            Log.d(TAG, "Second quadrant! " + String.valueOf(mag) + " " + String.valueOf(x_y_degree));
                        }

                        if (accelerX < 0 && accelerY < 0) {
                            quadOne.setText("");
                            quadTwo.setText("");
                            quadThree.setText("");
                            quadFour.setText(text);
                            quadrant = "four";
                            Log.d(TAG, "X : " + String.valueOf(accelerX) + " Y: " + String.valueOf(accelerY));

                            Log.d(TAG, "Fourth quadrant! " + String.valueOf(mag) + " " + String.valueOf(x_y_degree));
                        }

                    }

//                }


//            }

//            if(firstTimeOnSensorChange){
//                accelerYprev = accelerX;
//                accelerYprev = accelerY;
//                firstTimeOnSensorChange = false;
//
//            }else {
//
//                if (accelerX * accelerXprev < 0 && accelerY * accelerYprev < 0) {
//                    Log.d(TAG, "It's decelerating!!");
//                    sensorManager.unregisterListener(this);
//                    firstTimeOnSensorChange = true;
//                } else {
//
//
//                    if (accelerX > -0.2) {
//                        if (accelerX < 0.2) {
//                            accelerX = 0;
//                        }
//                    }
//
//                    if (accelerY > -0.2) {
//                        if (accelerY < 0.2) {
//                            accelerY = 0;
//                        }
//                    }
//
//                    accelerXprev =  accelerX;
//                    accelerYprev = accelerY;
//
//                    double mag = Math.sqrt(accelerX * accelerX + accelerY * accelerY);
//
//                    x_y_degree = Math.toDegrees(Math.atan2(accelerY, accelerX));
//
//                    if (mag > 0.5) {
//                        String text = "Mag:" + String.valueOf(mag) + " Degree:" + String.valueOf(x_y_degree);
//                        if (acceler[0] > 0 && acceler[1] > 0) {
//                            quadFour.setText("");
//                            quadThree.setText("");
//                            quadTwo.setText("");
//                            quadOne.setText(text);
//                            quadrant = "one";
//
//                            Log.d(TAG, "X : " + String.valueOf(acceler[0]) + " Y: " + String.valueOf(acceler[1]));
//
//                            Log.d(TAG, "First quadrant! " + String.valueOf(mag) + " " + String.valueOf(x_y_degree));
//                        }
//
//                        if (acceler[0] < 0 && acceler[1] > 0) {
//                            quadFour.setText("");
//                            quadOne.setText("");
//                            quadTwo.setText("");
//                            quadThree.setText(text);
//                            quadrant = "three";
//                            Log.d(TAG, "X : " + String.valueOf(acceler[0]) + " Y: " + String.valueOf(acceler[1]));
//
//
//                            Log.d(TAG, "Third quadrant! " + String.valueOf(mag) + " " + String.valueOf(x_y_degree));
//                        }
//                        if (acceler[0] > 0 && acceler[1] < 0) {
//                            quadOne.setText("");
//                            quadFour.setText("");
//                            quadThree.setText("");
//                            quadTwo.setText(text);
//                            quadrant = "two";
//                            Log.d(TAG, "X : " + String.valueOf(acceler[0]) + " Y: " + String.valueOf(acceler[1]));
//
//                            Log.d(TAG, "Second quadrant! " + String.valueOf(mag) + " " + String.valueOf(x_y_degree));
//                        }
//
//                        if (acceler[0] < 0 && acceler[1] < 0) {
//                            quadOne.setText("");
//                            quadTwo.setText("");
//                            quadThree.setText("");
//                            quadFour.setText(text);
//                            quadrant = "four";
//                            Log.d(TAG, "X : " + String.valueOf(acceler[0]) + " Y: " + String.valueOf(acceler[1]));
//
//                            Log.d(TAG, "Fourth quadrant! " + String.valueOf(mag) + " " + String.valueOf(x_y_degree));
//                        }
//
//                    }
//                }
//            }


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
//        handler.removeCallbacks(runable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
//        startTimer_main();

//        handler.post(runable);
    }

    /**
     * Update the acceleration sensor output Text Views.
     */
//    protected void updateAccelerationText()
//    {
//        Log.d(TAG, "inside update acceleration text");
//        textViewXAxis.setText(String.format("%.2f", linearAcceleration[0]));
//        textViewYAxis.setText(String.format("%.2f", linearAcceleration[1]));
//        textViewZAxis.setText(String.format("%.2f", linearAcceleration[2]));
//
//    }

    public void startTimer_main() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Log.d(TAG, String.valueOf(listX.size()));
//                        Log.d(TAG, String.valueOf(listY.size()));

                        listX = lpfAccelSmoothing.addSamples(listX);
                        listY = lpfAccelSmoothing.addSamples(listY);

                        float sum = 0;
                        for (float d : listX) sum += d;
                        float average = sum / listX.length;
                        Log.d(TAG, "Average accel in x per sec : " + String.valueOf(average));


                        listX = null;
                        listY = null;

                        count = 0;
                    }
                });
                Log.d("TIMER", "STOPPED");
            }
        }, 500, 1000);
    }


//    protected float[] lowPass( float[] input, float[] output ) {
//        if ( output == null ) return input;
//        for ( int i=0; i<input.length; i++ ) {
//            output[i] = output[i] + ALPHA * (input[i] - output[i]);
//        }
//        return output;
//    }


}
