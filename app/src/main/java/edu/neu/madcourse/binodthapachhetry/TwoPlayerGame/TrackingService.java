package edu.neu.madcourse.binodthapachhetry.TwoPlayerGame;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import edu.neu.madcourse.binodthapachhetry.Communication.CommunicationMain;

public class TrackingService extends Service
{
    private static final String TAG = "TrackingService";
    public static final String BROADCAST_ACTION = "Hello World";
    private final static int MIN_DISTANCE = 0;
    private final static int MIN_TIME = 5000;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public static double longitude;
    public static double latitude;
    TwoPlayerGameRemoteClient remoteClient;
    private static String uName;

    Intent intent;
    int counter = 0;

//    public TrackingService(String uName){
//        super();
//        this.uName = uName;
//    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        Log.d(TAG, "ON CREATE");
        intent = new Intent(BROADCAST_ACTION);
        remoteClient = new TwoPlayerGameRemoteClient(this);
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        Log.d(TAG, "ON START");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, listener);
        uName = intent.getStringExtra("user_name");
//        uName = TwoPlayerGameMain.myName;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

//    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
//        if (currentBestLocation == null) {
//            // A new location is always better than no location
//            return true;
//        }
//
//        // Check whether the new location fix is newer or older
//        long timeDelta = location.getTime() - currentBestLocation.getTime();
//        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
//        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
//        boolean isNewer = timeDelta > 0;
//
//        // If it's been more than two minutes since the current location, use the new location
//        // because the user has likely moved
//        if (isSignificantlyNewer) {
//            return true;
//            // If the new location is more than two minutes older, it must be worse
//        } else if (isSignificantlyOlder) {
//            return false;
//        }
//
//        // Check whether the new location fix is more or less accurate
//        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
//        boolean isLessAccurate = accuracyDelta > 0;
//        boolean isMoreAccurate = accuracyDelta < 0;
//        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
//
//        // Check if the old and new location are from the same provider
//        boolean isFromSameProvider = isSameProvider(location.getProvider(),
//                currentBestLocation.getProvider());
//
//        // Determine location quality using a combination of timeliness and accuracy
//        if (isMoreAccurate) {
//            return true;
//        } else if (isNewer && !isLessAccurate) {
//            return true;
//        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
//            return true;
//        }
//        return false;
//    }



    /** Checks whether two providers are the same */
//    private boolean isSameProvider(String provider1, String provider2) {
//        if (provider1 == null) {
//            return provider2 == null;
//        }
//        return provider1.equals(provider2);
//    }



    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.d(TAG, "ON DESTROYED");
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }




    public class MyLocationListener implements LocationListener
    {

        public void onLocationChanged(Location loc)
        {
            Log.d(TAG, "Location changed");
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
            Log.d(TAG, String.valueOf(latitude));
            Log.d(TAG, String.valueOf(longitude));
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("longitude", longitude);
            updates.put("latitude",latitude);
            Log.d(TAG, uName);
            remoteClient.saveValue(uName,updates);
            // add this to firebase


//            if(isBetterLocation(loc, previousBestLocation)) {
//                loc.getLatitude();
//                loc.getLongitude();
//                intent.putExtra("Latitude", loc.getLatitude());
//                intent.putExtra("Longitude", loc.getLongitude());
//                intent.putExtra("Provider", loc.getProvider());
//                sendBroadcast(intent);
//
//            }
        }

        public void onProviderDisabled(String provider)
        {
//            Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
        }


        public void onProviderEnabled(String provider)
        {
//            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }
}
