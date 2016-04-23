package edu.neu.madcourse.binodthapachhetry.FindingAstro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.neu.madcourse.binodthapachhetry.R;

/**
 * Created by adityaponnada on 04/04/16.
 */
public class skyview_activity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "skyview_activity";
    private static final String SCORE = " SCORE: ";
    private static final float maxAcceleration = 5;
    private static final float minAcceleration = 2;
    private static int score = 0;
    private static int level = 0;

    Float azimut;
    Float roll;
    Float pitch;
    List<Float> accelerXdir = new ArrayList<Float>();
    List<Float> accelerYdir = new ArrayList<Float>();
    List<Float> accelerMag = new ArrayList<Float>();

    MediaPlayer mMediaPlayer;

    FrameLayout frameLayout;
    CustomDrawableView mCustomDrawableView;

    Vibrator vibrator;

    boolean button_left = false;
    boolean button_right = false;

    // View to draw a compass
    public class CustomDrawableView extends View {
        Paint paint = new Paint();
        public CustomDrawableView(Context context) {
            super(context);
            paint.setColor(0xff00ff00);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setAntiAlias(true);
        };

        public CustomDrawableView(Context context, AttributeSet attributeSet){
            super(context, attributeSet);
            paint.setColor(0xff00ff00);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setAntiAlias(true);

        };

        public CustomDrawableView(Context context, AttributeSet attributeSet, int default_style){
            super(context, attributeSet, default_style);
            paint.setColor(0xff00ff00);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setAntiAlias(true);
            //empty constructor
        };


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();
            int centerx = width/2;
            int centery = height/2;
            canvas.drawLine(centerx, 0, centerx, height, paint);
            canvas.drawLine(0, centery, width, centery, paint);
            // Rotate the canvas with the azimut
            if (azimut != null)
                canvas.rotate(-azimut*360/(2*3.14159f), centerx, centery);
            paint.setColor(0xffff0000);
            canvas.drawLine(centerx, -1000, centerx, +1000, paint);
            canvas.drawLine(-1000, centery, 1000, centery, paint);
            paint.setColor(0xff00ff00);
        }
    }
    //CustomDrawableView mCustomDrawableView;
    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    Sensor linearAccelerometer;
    private LowPassFilterSmoothing lpfAccelSmoothing;

    ImageView arrow_Right;

    Timer timer;

    ImageButton holdButton_left;
    ImageButton holdButton_right;
    ImageView target;

    public ArrayList<Animation> animationArray = new ArrayList<>();
    public ArrayList<Integer> xDirection = new ArrayList<>();
    public ArrayList<Integer> yDirection = new ArrayList<>();
    public Integer currentXdirection;
    public Integer currentYdirection;
    public ArrayList<Integer> direction_feedback = new ArrayList<>();
//    public Integer maxXint;
//    public Integer maxYint;

//    private AlertDialog mDialog;

    ImageView astroImage;
    TextView scoreText;
    Animation left_to_right_animation, right_to_left_animation, vertical_up_animation,
            vertical_down_animation, south_east_animation, south_west_animation,
            north_east_animation, north_west_animation, return_anim;

    Random random;
    int AnimArraySize;
    int min = 0;
    int randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mCustomDrawableView = new CustomDrawableView(getApplicationContext());
        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(R.layout.skyview);

        scoreText = (TextView) findViewById(R.id.snap_score);

        lpfAccelSmoothing = new LowPassFilterSmoothing();

        //********UI elements **********
        astroImage = (ImageView)findViewById(R.id.astro);

        holdButton_left = (ImageButton)findViewById(R.id.left_hold);
        holdButton_right = (ImageButton)findViewById(R.id.right_hold);
        arrow_Right = (ImageView)findViewById(R.id.move_right);

        target = (ImageView)findViewById(R.id.target);

        frameLayout = (FrameLayout)findViewById(R.id.frame_layout);
        frameLayout.addView(mCustomDrawableView);

        //*****************************

        //************ Animation Declarations ******************
        vertical_up_animation = AnimationUtils.loadAnimation(this, R.anim.vertical_up);
        vertical_down_animation = AnimationUtils.loadAnimation(this, R.anim.vertical_down);
        left_to_right_animation = AnimationUtils.loadAnimation(this, R.anim.left_to_right);
        right_to_left_animation = AnimationUtils.loadAnimation(this, R.anim.right_to_left);
        north_east_animation = AnimationUtils.loadAnimation(this, R.anim.north_east);
        north_west_animation = AnimationUtils.loadAnimation(this, R.anim.north_west);
        south_east_animation = AnimationUtils.loadAnimation(this, R.anim.south_east);
        south_west_animation = AnimationUtils.loadAnimation(this, R.anim.south_west);

        return_anim = AnimationUtils.loadAnimation(this, R.anim.return_anim);


        //*****************************************************

        //************* ALL ANIMATIONS HERE ***************
        animationArray.add(left_to_right_animation);
        xDirection.add(0);
        yDirection.add(-1);
        direction_feedback.add(R.drawable.right_arrow);

        animationArray.add(right_to_left_animation);
        xDirection.add(0);
        yDirection.add(1);
        direction_feedback.add(R.drawable.left_arrow);

        animationArray.add(vertical_up_animation);
        xDirection.add(1);
        yDirection.add(0);
        direction_feedback.add(R.drawable.up_arrow);

        animationArray.add(vertical_down_animation);
        xDirection.add(-1);
        yDirection.add(0);
        direction_feedback.add(R.drawable.down_arrow);

        animationArray.add(north_east_animation);
        xDirection.add(1);
        yDirection.add(-1);
        direction_feedback.add(R.drawable.up_right_arrow);

        animationArray.add(north_west_animation);
        xDirection.add(1);
        yDirection.add(1);
        direction_feedback.add(R.drawable.up_left_arrow);

        animationArray.add(south_east_animation);
        xDirection.add(-1);
        yDirection.add(-1);
        direction_feedback.add(R.drawable.down_right_arrow);

        animationArray.add(south_west_animation);
        xDirection.add(-1);
        yDirection.add(1);
        direction_feedback.add(R.drawable.down_left_arrow);


        //*************************************************

        //******** Misc Declarations*********
        random = new Random();
        //***********************************

        //****** SENSING*******
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        linearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        //****************

        holdButton_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        button_left= true;
                        if (button_right == true){
                            startTimer_main();
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        button_left = false;
//                        startTimer();
                        break;
                    }
                }
                return true;
            }
        });

        holdButton_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        button_right = true;

                        if (button_left == true){
                            startTimer_main();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("ACTION_UP", "LIFTED");
                        button_right = true;
//                        startTimer();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.findingastro_background);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.setVolume(.5f, .5f);
        mMediaPlayer.start();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this,linearAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stoptimertask();
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
    }

    @Override
    public void onPause() {
        super.onPause();

        stoptimertask();
        mSensorManager.unregisterListener(this);

        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();

    }

    public void startTimer_main(){
        timer = new Timer();
        AnimArraySize = animationArray.size();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        randomNumber = random.nextInt(AnimArraySize - 1);
                        astroImage.startAnimation(animationArray.get(randomNumber));

                        currentXdirection = xDirection.get(randomNumber);
                        currentYdirection = yDirection.get(randomNumber);

                        Log.d("RANDOMIZED", String.valueOf(randomNumber));
                        Log.d("RANDOMIZED_SIZE", String.valueOf(AnimArraySize));
                        arrow_Right.setImageResource(direction_feedback.get(randomNumber));
                        arrow_Right.setVisibility(View.VISIBLE);
                        Log.d("TIMER", "RUNNING");
                        stoptimertask();
                        startTimer_moveTime();
                    }
                });
                Log.d("TIMER", "STOPPED");

            }
        }, 2000, 10000);
        arrow_Right.setVisibility(View.GONE);
    }
    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            Log.d("STOPPED", "TIMER STOPPED");
            timer = null;
        }
    }

    public void startTimer(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        astroImage.startAnimation(left_to_right_animation);
                        arrow_Right.setVisibility(View.VISIBLE);


                        Log.d("TIMER", "RUNNING");
                        stoptimertask();
                    }
                });

                Log.d("TIMER", "STOPPED");
            }
        }, 5000, 10000);
        arrow_Right.setVisibility(View.GONE);
    }


    public void startTimer_moveTime(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(accelerMag.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Time's up for the move", Toast.LENGTH_LONG).show();
                        }else {

                            float maximum = Collections.max(accelerMag);
                            float max = accelerMag.get(0);
                            int index = 0;

                            for(int i = 0; i < accelerMag.size(); i++) {
                                float number = accelerMag.get(i);
                                if(number > max){
                                    max = number;
                                    index = i;
                                }
                            }
                            // get the x and y direction
                            float maxX = accelerXdir.get(index);
                            float maxY = accelerYdir.get(index);
                            double x_y_degree = Math.toDegrees(Math.atan2(maxY, maxX)); // not sure if we need it

                            if(Math.abs(maxX) < 0.25){
                                maxX = 0;
                            }
                            if(Math.abs(maxY) < 0.25){
                                maxY = 0;
                            }

                            Log.d(TAG, String.valueOf(maxX) + "," + String.valueOf(maxY));
                            Integer maxXint = Math.round(Math.signum(maxX));
                            Integer maxYint = Math.round(Math.signum(maxY));

                            Log.d(TAG,"Magnitude: " + String.valueOf(max)+ " X sign: " + String.valueOf(maxXint) + " Y sign: " + String.valueOf(maxYint)+" Xdir: " + String.valueOf(currentXdirection) + " Ydir: " + String.valueOf(currentYdirection));
                            if(max > 0.5 && currentXdirection.intValue() == maxXint.intValue() && currentYdirection.intValue() == maxYint.intValue()){
//                                Log.d(TAG,"X sign: " + String.valueOf(maxXint) + " Y sign: " + String.valueOf(maxYint)+" Xdir: " + String.valueOf(currentXdirection) + " Ydir: " + String.valueOf(currentYdirection));
                                Log.d(TAG, "Conditions met!!");
//                                astroImage.setVisibility(View.VISIBLE);
                                astroImage.startAnimation(return_anim);
                                score += 1;
                                scoreText.setText(SCORE + String.valueOf(score));


//                                if(score == 4){
//                                    level +=1;
//                                }

                                CharSequence text = "Hold off and then on, one of your thumbs to take picture!";
                                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                                toast.show();

                                accelerMag.clear();

                            }
                            else{

                                openAlert();
                            }
                        }

                        stoptimertask();
                    }
                });
                Log.d("MOVE TIMER", "STOPPED");
            }
        }, 2000, 10000);
    }

    float[] mGravity;
    float[] mGeomagnetic;
    float[] mAcceleration;


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
            mGeomagnetic = event.values;

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
            mAcceleration = event.values;

        if(mAcceleration != null){
            mAcceleration = lpfAccelSmoothing.addSamples(mAcceleration);
            float accelerX = mAcceleration[0];
            float accelerY = mAcceleration[1];

            accelerXdir.add(accelerX);
            accelerYdir.add(accelerY);

            float mag = (float) Math.sqrt(accelerX * accelerX + accelerY * accelerY);
            accelerMag.add(mag);

        }

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[1];
                if (azimut >= 0.2 || azimut <= -0.2){
//                    vibrator.vibrate(500);
                    Log.d("AZIMUT DETECTED", String.valueOf(azimut));
                }

//                roll = orientation[0];
//
//                if (roll >=3.0 || roll <= -3.0){
//                    vibrator.vibrate(500);
//                    Log.d("ROLL DETECTED", String.valueOf(roll));
//                }
//                pitch = orientation[2];
//                if(pitch >= 3.0 || pitch <= -3.0){
//                    vibrator.vibrate(500);
//                    Log.d("PITCH DETECTED", String.valueOf(pitch));
//
//                    mMediaPlayer.setVolume(0.5f, 0.5f);
//                    mMediaPlayer.start();
//                }
            }
        }
        mCustomDrawableView.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void openAlert(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(skyview_activity.this);
        builder.setMessage(R.string.failed_move);

        builder.setPositiveButton(R.string.quit_game,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,
                                        int i) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });


        builder.setNegativeButton(R.string.restart_game,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,
                                        int i) {
                        Intent intent = new Intent(getApplicationContext(), FindingAstroMain.class);
                        startActivity(intent);
                    }
                });



        // create alert dialog
        AlertDialog mDialog = builder.create();

        mDialog.show();

    }
}