package edu.neu.madcourse.binodthapachhetry.FindingAstro;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class RotationVector extends Activity implements SensorEventListener {

    private final String TAG = "RotationVector";

    Float azimut;  // View to draw a compass
    Float roll;
    Float pitch;
    Float scalarComponent;
    ArrayList<Float> azimutArray = new ArrayList<>();
    ArrayList<Float> rollArray = new ArrayList<>();
    ArrayList<Float> pitchArray = new ArrayList<>();
    ArrayList<Float> scalarArray = new ArrayList<>();

    Float azimut_diff = 0.0f;
    Float roll_diff = 0.0f;
    Float pitch_diff = 0.0f;
    Float scalar_diff = 0.0f;
    Vibrator vibrator;

    float d_roll;
    float d_pitch;
    float d_yaw;


    public class CustomDrawableView extends View {
        Paint paint = new Paint();
        public CustomDrawableView(Context context) {
            super(context);
            paint.setColor(0xff00ff00);
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setAntiAlias(true);
            vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
        };

        protected void onDraw(Canvas canvas) {
            int width = getWidth();
            int height = getHeight();
            int centerx = width/2;
            int centery = height/2;
            canvas.drawLine(centerx, 0, centerx, height, paint);
            canvas.drawLine(0, centery, width, centery, paint);
            // Rotate the canvas with the azimut
//            if (azimut != null)
//                canvas.rotate(-azimut*360/(2*3.14159f), centerx, centery);

            if (d_roll!=0.0)
                canvas.rotate((float) d_roll, centerx, centery);

            paint.setColor(0xff0000ff);
            canvas.drawLine(centerx, -1000, centerx, +1000, paint);
            canvas.drawLine(-1000, centery, 1000, centery, paint);
            canvas.drawText("N", centerx+5, centery-10, paint);
            canvas.drawText("S", centerx-10, centery+15, paint);
            paint.setColor(0xff00ff00);
        }
    }

    CustomDrawableView mCustomDrawableView;
    private SensorManager mSensorManager;
    //Sensor accelerometer;
   // Sensor magnetometer;
    //Sensor magnetometer_mgField;
    Sensor RotationVectorSensor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);    // Register the sensor listeners
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // magnetometer_mgField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
       // magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        RotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    protected void onResume() {
        super.onResume();
       // mSensorManager.registerListener(this, magnetometer_mgField, SensorManager.SENSOR_DELAY_UI);
        //mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, RotationVectorSensor, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {  }

    public void onSensorChanged(SensorEvent event) {

        float[] result = new float[3];
        float[] orientation = new float[3];
        float[] rotMat = new float[16];
        SensorManager.getRotationMatrixFromVector(rotMat,event.values);
        SensorManager.getOrientation(rotMat, orientation);

        d_roll = (float) Math.toDegrees(orientation[0]);
        d_pitch = (float) Math.toDegrees(orientation[1]);
        d_yaw = (float) Math.toDegrees(orientation[2]);



//        double x = event.values[0];
//        double y = event.values[1];
//        double z = event.values[2];
//        double w = event.values[3];
//
//        double sqx = x*x;
//        double sqy = y*y;
//        double sqz = z*z;
//        double sqw = w*w;
//
//        double unit = sqx + sqy + sqz + sqw;
//        double test = x * y + z * w;

//        d_roll = Math.toDegrees(Math.atan2(2 * y * w - 2 * x * z, sqx - sqy - sqz + sqw)); // roll or heading
//        d_pitch = Math.toDegrees(Math.asin(2 * test / unit)); // pitch or attitude
//        d_yaw = Math.toDegrees(Math.atan2(2 * x * w - 2 * y * z, -sqx + sqy - sqz + sqw)); // yaw or bank

//        d_roll = Math.toDegrees(Math.atan2(2 * z * w + 2 * x * y, sqx - sqy - sqz + sqw)); // roll or heading
//        d_pitch = Math.toDegrees(Math.asin(-2 * x * z + 2 * y * w)); // pitch or attitude
//        d_yaw = Math.toDegrees(Math.atan2(2 * x * w + 2 * y * z, -sqx - sqy + sqz + sqw)); // yaw or bank

//        Log.d(TAG,"Roll : " + String.valueOf(d_roll));

//        if(Math.abs(d_roll) > 4.0 && Math.abs(d_pitch)>4 && Math.abs(d_roll)>4) {
//
            Log.d(TAG, String.valueOf(d_roll) + " " + String.valueOf(d_pitch) + " " + String.valueOf(d_yaw));
//        }


//        azimut = event.values[0]; // orientation contains: azimut, pitch and roll
//        //Log.d("AZIMUTH", String.valueOf(Math.toDegrees(azimut)));
//        azimutArray.add(azimut);
//        for (int i=1; i<azimutArray.size();i++){
//            azimut_diff = azimutArray.get(i) - azimutArray.get(i-1);
//           // Log.d("AZIMUT_DIFF", String.valueOf(azimut_diff));
//        }
//
//        scalarComponent = event.values[3];
//        scalarArray.add(scalarComponent);
//       // Log.d("SCALAR COMPONENT", String.valueOf(scalarComponent));
//        for (int i=1; i<scalarArray.size();i++){
//            scalar_diff = scalarArray.get(i) - scalarArray.get(i-1);
////            Log.d("SCALAR DIFF", String.valueOf(scalar_diff));
//        }
//
//        if (scalar_diff>=0.005 || scalar_diff <= -0.005){
//            Toast.makeText(this, "Please keep the phone straight", Toast.LENGTH_SHORT).show();
//            vibrator.vibrate(500);
//        }
//
//        roll = event.values[2];
//        //Log.d("ROLL", String.valueOf(Math.toDegrees(roll)));
//        pitch = event.values[1];
        //Log.d("PITCH", String.valueOf(Math.toDegrees(pitch)));
    }
}