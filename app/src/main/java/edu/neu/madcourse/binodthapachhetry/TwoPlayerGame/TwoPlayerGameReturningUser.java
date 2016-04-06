package edu.neu.madcourse.binodthapachhetry.TwoPlayerGame;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationServices;

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

public class TwoPlayerGameReturningUser extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TEXT_VIEW = "Players ordered based on proximity to you";
    private static final String TEXT_VIEW_ALTERNATIVE = "No players found";
    private static final String TAG = "TwoPlayerReturningUser";
    private final static int MIN_DISTANCE = 0;
    private final static int MIN_TIME = 0;
    private final static String REG_ID = "regID";
    private final static String HIGH_SCORE = "highScore";
    private final static String LATITUDE = "latitude";
    private final static String LONGITUDE = "longitude";


    public static String regid;
    public static String yourRegID;
    public static String yourHighScore;
    public static String myName;
    public static String opponentName;
    private static double myLongitude;
    private static double myLatitude;
    private static double yourLatitude = 0;
    private static double yourLongitude = 0;
    private static int yourScore = 0;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private GoogleApiClient mGoogleApiClient;

    GoogleCloudMessaging gcm;
    Button enterGameButton;
    Button findOpponentButton;
    EditText rMessage;
    Context context;
    TextView mDisplay;

    TwoPlayerGameRemoteClient remoteClient;
    Timer timer;
    TimerTask timerTask;
    private static HashMap<String, HashMap<String, Object>> hMap;
    List<String> opponentKeys = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player_game_returning_user);
        remoteClient = new TwoPlayerGameRemoteClient(this);
        hMap = remoteClient.getHash();


        mDisplay = (TextView) findViewById(R.id.two_player_game_text_view);

        rMessage = (EditText) findViewById(R.id.two_player_game_registeredusertextBox);

        findOpponentButton = (Button) findViewById(R.id.two_player_game_registered_user_button);

        enterGameButton = (Button) findViewById(R.id.two_player_game_enter_game);
        enterGameButton.setEnabled(false);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    protected  void onResume(){
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.two_player_game_registered_user_button)) {
            if (rMessage.getText().length() != 0) {
                myName = rMessage.getText().toString();

                Log.d(TAG, String.valueOf(myLatitude));
                Log.d(TAG, String.valueOf(myLongitude));
                Map<String, Object> updates = new HashMap<String, Object>();
                updates.put("longitude", myLongitude);
                updates.put("latitude", myLatitude);
                Log.d(TAG, "Longitude" + String.valueOf(myLongitude));
                Log.d(TAG, "Latitude" + String.valueOf(myLatitude));

                remoteClient.saveValue(myName, updates);
                Log.d(TAG, String.valueOf(hMap));

                if (hMap.size() <= 1) {
                    mDisplay.setText(TEXT_VIEW_ALTERNATIVE);

                } else {
                    mDisplay.setText(TEXT_VIEW);
                    RadioGroup opponentsRadioGroup = (RadioGroup) findViewById(R.id.two_player_game_player_list);

                    hMap.remove(rMessage.getText().toString());


                    HashMap<String, HashMap<String, Object>> newHash = (HashMap<String, HashMap<String, Object>>) hMap;

                    int radioButtonID = 0;

                    Iterator<Map.Entry<String, HashMap<String, Object>>> iterator = newHash.entrySet().iterator();

                    TreeMap<Double, String> tempTree = new TreeMap<Double, String>();

                    while (iterator.hasNext()) {

                        Map.Entry<String, HashMap<String, Object>> entry = (Map.Entry<String, HashMap<String, Object>>) iterator.next();
                        if (!opponentKeys.contains(entry.getKey().toString())) {
                            Log.d(TAG, String.valueOf(entry.getKey()));

                            HashMap<String, Object> tempMap = (HashMap<String, Object>) entry.getValue();
                            for (Map.Entry<String, Object> entryIn : tempMap.entrySet()) {
                                if (entryIn.getKey().toString() == LATITUDE) {
                                    yourLatitude = (Double) entryIn.getValue();
                                }
                                if (entryIn.getKey().toString() == LONGITUDE) {
                                    yourLongitude = (Double) entryIn.getValue();
                                }
                            }
                            // get geodesic distance
                            Double distance = Math.sqrt(Math.pow((myLatitude - yourLatitude), 2) + Math.pow((myLongitude - yourLongitude), 2));
                            tempTree.put(distance, entry.getKey().toString());
                            Log.d(TAG, String.valueOf(distance));
                        }
                    }

                    Log.d(TAG, "tree map");
                    Log.d(TAG, String.valueOf(tempTree));

                    NavigableSet nset = tempTree.descendingKeySet();
                    Log.d(TAG, String.valueOf(nset));

                    List l = new ArrayList();

                    Iterator it = nset.iterator();
                    while (it.hasNext()) {
                        l.add(it.next());
                    }
                    Collections.reverse(l);
                    Log.d(TAG, String.valueOf(l));

                    Iterator<Double> crunchifyIterator = l.iterator();


                    while (crunchifyIterator.hasNext()) {
                        String tempKey = tempTree.get(crunchifyIterator.next());
                        if (!opponentKeys.contains(tempKey)) {
                            HashMap<String, Object> tempMap = (HashMap<String, Object>) newHash.get(tempKey);

                            for (Map.Entry<String, Object> entryIn : tempMap.entrySet()) {
                                if (entryIn.getKey().toString() == REG_ID) {
                                    yourRegID = (String) entryIn.getValue();
                                }
                                if (entryIn.getKey().toString() == HIGH_SCORE) {
                                    yourHighScore = String.valueOf(entryIn.getValue());
                                }
                            }
                            opponentKeys.add(tempKey);
                            RadioButton radioButton = new RadioButton(this);
                            radioButton.setId(radioButtonID);
                            radioButton.setText(tempKey + " High Score: " + String.valueOf(yourHighScore));
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
                                sendMessage("Invitation from " + rMessage.getText().toString() + " to play Two player Scraggle!");
                                    enterGameButton.setEnabled(true);
                                    findOpponentButton.setEnabled(false);

                                    enterGameButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent myIntent = new Intent(getApplicationContext(), edu.neu.madcourse.binodthapachhetry.TwoPlayerGame.TwoPlayerGameScraggleGameActivity.class);
                                            startActivity(myIntent);
                                        }
                                    });
                                }
                            });
                        }
                    }

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
                TwoPlayerGameGcmNotification twoPlayerGameGcmNotification = new TwoPlayerGameGcmNotification();
                regIds.clear();
                regIds.add(reg_device);
                Log.d(TAG,"sending gcm notification for two player game");
                twoPlayerGameGcmNotification.sendNotification(msgParams, regIds,TwoPlayerGameReturningUser.this);
                return "Message Sent - " + message;
            }

            @Override
            protected void onPostExecute(String msg) {
//                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);

}

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            Log.i(TAG, "Location null.");
        }
        else {
            myLatitude = (Double) location.getLatitude();
            myLongitude = (Double) location.getLongitude();
            Log.d(TAG, "Longitude " + String.valueOf(myLongitude));
            Log.d(TAG, "Latitude " + String.valueOf(myLatitude));

        };

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
