package edu.neu.madcourse.binodthapachhetry.FindingAstro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.neu.madcourse.binodthapachhetry.Communication.CommunicationConstants;
import edu.neu.madcourse.binodthapachhetry.R;
import edu.neu.madcourse.binodthapachhetry.TwoPlayerGame.TwoPlayerGameRemoteClient;

public class FindingAstroNewUser extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "FindingAstroNewUser";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TEXT_DISPLAY = "You are registered! Please press Enter As a Registered User button.";
    public static final String PROPERTY_REG_ID = "registration_id";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static int LEVEL = 0;


    GoogleCloudMessaging gcm;
    Button registerUserButton;
    Button enterGameButton;
    EditText rMessage;
    Context context;
    public static String regid;
    FindingAstroRemoteClient remoteClient;
    TextView mDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_astro_new_user);
        remoteClient = new FindingAstroRemoteClient(this);
        gcm = GoogleCloudMessaging.getInstance(this);
        context = getApplicationContext();
        regid = getRegistrationId(context);

        mDisplay = (TextView) findViewById(R.id.finding_astro_display);
        rMessage = (EditText) findViewById(R.id.finding_astro_newusertextBox);

        registerUserButton = (Button) findViewById(R.id.finding_astro_new_user_button_second);
        enterGameButton = (Button) findViewById(R.id.finding_astro_enter_game_button);
        enterGameButton.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.finding_astro_new_user_button_second)) {
            if(rMessage.getText().length() != 0){
                if (checkPlayServices()) {
//                    regid = getRegistrationId(context);
                    Log.d(TAG, "regID: " + String.valueOf(regid));
                    if (TextUtils.isEmpty(regid)) {
                        registerInBackground();
                    }else{
                        Map<String, Object> update = new HashMap<String, Object>();
                        Log.d(TAG,regid);
                        update.put("regID", regid);
                        update.put("level",LEVEL);
                        remoteClient.saveValue(rMessage.getText().toString(), update);
                        mDisplay.append(TEXT_DISPLAY);
                        Log.d(TAG, "savedInFirebase: " + String.valueOf(rMessage.getText().length()));
                        enterGameButton.setEnabled(true);
                        registerUserButton.setEnabled(false);

                    }
                }
            }
        }

        if (view == enterGameButton) {
            Intent myIntent = new Intent(getApplicationContext(),edu.neu.madcourse.binodthapachhetry.FindingAstro.FindingAstroRegisteredUser.class);
            startActivity(myIntent);
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

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(FindingAstroMain.class.getSimpleName(), Context.MODE_PRIVATE);
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


}
