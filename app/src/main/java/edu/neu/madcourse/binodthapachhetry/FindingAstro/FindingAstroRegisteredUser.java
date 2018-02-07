package edu.neu.madcourse.binodthapachhetry.FindingAstro;

import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import edu.neu.madcourse.binodthapachhetry.R;

public class FindingAstroRegisteredUser extends AppCompatActivity implements View.OnClickListener{

    private static final String TEXT_VIEW = "Players :";
    private static final String TEXT_VIEW_ALTERNATIVE = "No players found";
    private static final String TAG = "FindingAstroRegistered";
    private final static int MIN_DISTANCE = 0;
    private final static int MIN_TIME = 0;
    private final static String REG_ID = "regID";
    private final static String LEVEL = "level";

    public static String regid;
    public static String yourRegID;
    public static String yourHighScore;
    public static String myName;
    public static String opponentName;

    private static String yourLevel;
    public static int myLevel;

    GoogleCloudMessaging gcm;
    Button enterGameButton;
    Button findOpponentButton;
    Button singlePlayerButton;
    EditText rMessage;
    Context context;
    TextView mDisplay;

    FindingAstroRemoteClient remoteClient;
    Timer timer;
    TimerTask timerTask;
    private static HashMap<String, HashMap<String, Object>> hMap;
    List<String> opponentKeys = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_astro_registered_user);

        remoteClient = new FindingAstroRemoteClient(this);
        hMap = remoteClient.getHash();

        mDisplay = (TextView) findViewById(R.id.finding_astro_text_view);
        rMessage = (EditText) findViewById(R.id.finding_astro_registeredusertextBox);
        singlePlayerButton = (Button) findViewById(R.id.finding_astro_single_player);
        findOpponentButton = (Button) findViewById(R.id.finding_astro_registered_user_button);
        enterGameButton = (Button) findViewById(R.id.finding_astro_enter_game);
        enterGameButton.setEnabled(false);

    }

    @Override
    protected  void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View view) {

        if(view == findViewById(R.id.finding_astro_single_player)){
            if (rMessage.getText().length() != 0) {
                myName = rMessage.getText().toString();
                Intent myIntent = new Intent(getApplicationContext(), edu.neu.madcourse.binodthapachhetry.FindingAstro.skyview_activity.class);
                startActivity(myIntent);
            }
        }
        if (view == findViewById(R.id.finding_astro_registered_user_button)) {
            if (rMessage.getText().length() != 0) {
                myName = rMessage.getText().toString();
                if(isOnline()) {

                    if (hMap.size() <= 1) {
                        mDisplay.setText(TEXT_VIEW_ALTERNATIVE);

                    } else {
                        mDisplay.setText(TEXT_VIEW);
                        RadioGroup opponentsRadioGroup = (RadioGroup) findViewById(R.id.finding_astro_player_list);

                        hMap.remove(rMessage.getText().toString());


                        HashMap<String, HashMap<String, Object>> newHash = (HashMap<String, HashMap<String, Object>>) hMap;

                        int radioButtonID = 0;

                        Iterator<Map.Entry<String, HashMap<String, Object>>> iterator = newHash.entrySet().iterator();

                        TreeMap<Double, String> tempTree = new TreeMap<Double, String>();

                        while (iterator.hasNext()) {

                            Map.Entry<String, HashMap<String, Object>> entry = (Map.Entry<String, HashMap<String, Object>>) iterator.next();
                            if (!opponentKeys.contains(entry.getKey().toString())) {
                                Log.d(TAG, String.valueOf(entry.getKey()));
                                String tempKey = entry.getKey().toString();

                                HashMap<String, Object> tempMap = (HashMap<String, Object>) entry.getValue();
                                for (Map.Entry<String, Object> entryIn : tempMap.entrySet()) {
                                    if (entryIn.getKey().toString() == REG_ID) {
                                        yourRegID = (String) entryIn.getValue();
                                    }
                                    if (entryIn.getKey().toString() == LEVEL) {
                                        yourLevel = String.valueOf(entryIn.getValue());
                                    }
                                }

                                opponentKeys.add(tempKey);
                                RadioButton radioButton = new RadioButton(this);
                                radioButton.setId(radioButtonID);
                                radioButton.setText(tempKey + " Level: " + String.valueOf(yourLevel));
                                final String userName = tempKey;
                                final String ID = yourRegID;
                                radioButtonID++;
                                opponentsRadioGroup.addView(radioButton);
                                Log.d("Added button: ", userName);

                                radioButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(v.getContext(), "Invited " + userName + " to play! ", Toast.LENGTH_LONG).show();
                                        regid = ID;
                                        opponentName = userName;
                                        sendMessage("Invitation from " + rMessage.getText().toString() + " to play Finding Astro!");
                                        enterGameButton.setEnabled(true);
                                        findOpponentButton.setEnabled(false);

                                        enterGameButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent myIntent = new Intent(getApplicationContext(), edu.neu.madcourse.binodthapachhetry.FindingAstro.skyview_activity.class);
                                                startActivity(myIntent);
                                            }
                                        });
                                    }
                                });


                            } else {
                                Log.d(TAG, "Myself " + String.valueOf(entry.getKey()));
                                String tempKey = entry.getKey().toString();

                                HashMap<String, Object> tempMap = (HashMap<String, Object>) entry.getValue();
                                for (Map.Entry<String, Object> entryIn : tempMap.entrySet()) {
                                    if (entryIn.getKey().toString() == LEVEL) {
                                        myLevel = (Integer) entryIn.getValue();
                                    }
                                }
                            }
                        }

                    }
                }else{
                    Log.d(TAG,"NO CONNECTIVITY");
                    Toast.makeText(getApplicationContext(),"Please check your internet connection!",Toast.LENGTH_LONG).show();
                }
            }
        }


    }

    private void sendMessage(final String message) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                List<String> regIds = new ArrayList<String>();
                String reg_device = regid;
                Map<String, String> msgParams;
                msgParams = new HashMap<>();
                msgParams.put("data.message", message);
                msgParams.put("data.myName",myName);
                msgParams.put("data.opponentName", opponentName);
                msgParams.put("data.myRegID",regid);
                msgParams.put("data.opponentRegId",yourRegID);
                Log.d(TAG, "My Reg id : " + regid);
                Log.d(TAG,"Your Reg id : " + yourRegID);
                FindingAstroGcmNotification findingAstroGcmNotification = new FindingAstroGcmNotification();
                regIds.clear();
                regIds.add(reg_device);
                Log.d(TAG,"sending gcm notification for two player game");
                findingAstroGcmNotification.sendNotification(msgParams, regIds,FindingAstroRegisteredUser.this);
                return "Message Sent - " + message;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);

    }


    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false; }
}
