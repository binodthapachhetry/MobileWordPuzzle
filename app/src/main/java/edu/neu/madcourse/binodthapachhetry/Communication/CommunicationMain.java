package edu.neu.madcourse.binodthapachhetry.Communication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import edu.neu.madcourse.binodthapachhetry.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jarvis on 3/22/16.
 */
public class CommunicationMain extends Activity implements View.OnClickListener {
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TEXT_DISPLAY = "You are registered! Please enter your username and press Find opponent button.";

    final Handler handler = new Handler();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "GCM Sample Demo";
    TextView mDisplay;
    EditText rMessage;
    EditText nMessage;
    Button buttonStart;
    GoogleCloudMessaging gcm;
    Context context;
    public static String regid;
    public static String myName;
    public static String opponentName;

    RemoteClient remoteClient;
    Timer timer;
    TimerTask timerTask;
    HashMap hmap;
    List<String> opponentKeys = new ArrayList<String>();


//    Context mContext;
//    public CommunicationMain(Context context) {
//        context = mContext;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);
        remoteClient = new RemoteClient(this);
        hmap = remoteClient.getHash();

        mDisplay = (TextView) findViewById(R.id.communication_display);
//        mMessage = (EditText) findViewById(R.id.communication_edit_message);
        rMessage = (EditText) findViewById(R.id.registeredusertextBox);
        nMessage = (EditText) findViewById(R.id.newusertextBox);
        buttonStart = (Button) findViewById(R.id.communication_enter_game);
        buttonStart.setEnabled(false);

//        buttonStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(getApplicationContext(), edu.neu.madcourse.binodthapachhetry.Communication.CommunicationScraggleMainActivity.class);
//                startActivity(myIntent);
//            }
//        });

        gcm = GoogleCloudMessaging.getInstance(this);


        context = getApplicationContext();


    }

    @Override
    public void onClick(final View view) {
//        if (view == findViewById(R.id.communication_send)) {
//            String message = ((EditText) findViewById(R.id.communication_edit_message)).getText().toString();
//            if (message.equals("")) {
//                Toast.makeText(context, "Sending Message Empty!", Toast.LENGTH_LONG).show();
//                return;
//            }
//            sendMessage(message);
//        }

//        if (view == findViewById(R.id.communication_clear)) {
//            mMessage.setText("");
//        }

//        if (view == findViewById(R.id.communication_unregistor_button)) {
//            unregister();
//        }

        if (view == findViewById(R.id.registered_user_button)) {
            Log.d("Registered button: ",String.valueOf(rMessage.getText().length()));
            if(rMessage.getText().length() != 0){
                myName = rMessage.getText().toString();
                RadioGroup opponentsRadioGroup = (RadioGroup) findViewById(R.id.communication_player_list);
//                HashMap hmap = remoteClient.getHash();

                Log.d("HashMap before: ",String.valueOf(hmap.size()));
                hmap.remove(rMessage.getText().toString());
                Log.d("HashMap after: ", String.valueOf(hmap.size()));
                int radioButtonID = 0;
                Set set2 = hmap.entrySet();
                Iterator iterator2 = set2.iterator();

                while(iterator2.hasNext()) {

                    Map.Entry entry = (Map.Entry)iterator2.next();
                    if (!opponentKeys.contains(entry.getKey().toString())) {
                        opponentKeys.add(entry.getKey().toString());
                        RadioButton radioButton = new RadioButton(this);
                        radioButton.setId(radioButtonID);
                        radioButton.setText(entry.getKey().toString());
                        final String userName = entry.getKey().toString();
                        final String ID = entry.getValue().toString();
                        radioButtonID++;
                        opponentsRadioGroup.addView(radioButton);
                        Log.d("Added button: ", userName);

                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "Invited " + userName + " to play! ", Toast.LENGTH_LONG).show();
                                regid = ID;
                                opponentName = userName;
                                sendMessage("Invitation from " + rMessage.getText().toString() + " to play. Pick "+ rMessage.getText().toString() + " in the opponent list to start playing!" );
                                buttonStart.setEnabled(true);
                                buttonStart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        Intent myIntent = new Intent(getApplicationContext(),edu.neu.madcourse.binodthapachhetry.Communication.CommunicationScraggleGameActivity.class);
                                        Intent myIntent = new Intent(getApplicationContext(),edu.neu.madcourse.binodthapachhetry.Communication.CommunicationScraggleGameActivity.class);

                                        startActivity(myIntent);
                                    }
                                });
                            }
                        });
                    }
                }
            }
        }

//        if (view == findViewById(R.id.communication_enter_game)) {
//            Log.d("Enter game press : ","jpt");
//            Intent intent = new Intent(this, CommunicationScraggleMainActivity.class);
//            startActivity(intent);
//
//        }

        if (view == findViewById(R.id.new_user_button)) {
            Log.d("After if view : ",String.valueOf(nMessage.getText().length()));
            if (checkPlayServices()) {
                Log.d("After if check: ",String.valueOf(nMessage.getText().length()));
                regid = getRegistrationId(context);
                Log.d("regID: ",String.valueOf(regid));
                if (TextUtils.isEmpty(regid)) {
                    registerInBackground();
                }else{
                    remoteClient.saveValue(nMessage.getText().toString(), regid);
                    mDisplay.append(TEXT_DISPLAY);
                    Log.d("savedInFirebase: ", String.valueOf(nMessage.getText().length()));
                }
            }
        }
    }

    private void sendMessage(final String message) {
        if (regid == null || regid.equals("")) {
            Toast.makeText(this, "You must register first", Toast.LENGTH_LONG).show();
            return;
        }
        if (message.isEmpty()) {
            Toast.makeText(this, "Empty Message", Toast.LENGTH_LONG).show();
            return;
        }
        Log.d(TAG,regid);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                List<String> regIds = new ArrayList<String>();
                String reg_device = regid;
                Map<String, String> msgParams;
                msgParams = new HashMap<>();
                msgParams.put("data.message", message);
                GcmNotification gcmNotification = new GcmNotification();
                regIds.clear();
                regIds.add(reg_device);
                gcmNotification.sendNotification(msgParams, regIds,CommunicationMain.this);
                return "Message Sent - " + message;
            }

            @Override
            protected void onPostExecute(String msg) {
//                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        Log.i(TAG, String.valueOf(registeredVersion));
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(CommunicationMain.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(CommunicationConstants.GCM_SENDER_ID);

                    // implementation to store and keep track of registered devices here
                    msg = "Device registered, registration ID=" + regid;
                    sendRegistrationIdToBackend();
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(TAG, msg);
                mDisplay.append(TEXT_DISPLAY);
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        // Your implementation here.
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void unregister() {
        Log.d(CommunicationConstants.TAG, "UNREGISTER USERID: " + regid);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    msg = "Sent unregistration";
                    gcm.unregister();
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                removeRegistrationId(getApplicationContext());
//                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                ((TextView) findViewById(R.id.communication_display))
                        .setText(regid);
            }
        }.execute();
    }

    private void removeRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(CommunicationConstants.TAG, "Removing regId on app version "
                + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_REG_ID);
        editor.commit();
        regid = null;
    }


    public void startTimer(String key) {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask(key);
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        // The values can be adjusted depending on the performance
        timer.schedule(timerTask, 2000, 1000);
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask(final String key) {
        timerTask = new TimerTask() {
            public void run() {
                Log.d(TAG, "isDataFetched >>>>" + remoteClient.isDataFetched());
                if(remoteClient.isDataFetched())
                {
                    handler.post(new Runnable() {

                        public void run() {
                            Log.d(TAG, "Value >>>>" + remoteClient.getValue(key));
                            regid = remoteClient.getValue(key);
//                            sendMessage(rMessage.getText().toString());
                        }
                    });

                    stoptimertask();
                }

            }
        };
    }


    }

