package edu.neu.madcourse.binodthapachhetry.FindingAstro;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jarvis on 4/22/16.
 */
public class FindingAstroRemoteClient {

    private static final String MyPREFERENCES = "MyPrefs" ;
    private static final String FIREBASE_DB = "https://astrofinding.firebaseio.com/";
    private static final String TAG = "TwoPlayerRemoteClient";
    private static boolean isDataChanged = false;
    private Context mContext;
    private HashMap<String, String> fireBaseData = new HashMap<String, String>();
    final HashMap<String, HashMap<String, Object>> hmap = new HashMap<String, HashMap<String, Object>>();



    public FindingAstroRemoteClient(Context mContext)
    {
        this.mContext = mContext;
        Firebase.setAndroidContext(mContext);

    }

    public HashMap<String, HashMap<String, Object>> getHash() {
        Firebase ref = new Firebase(FIREBASE_DB);
        Query queryRef = ref.orderByKey();
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot chld : snapshot.getChildren()) {
                    // snapshot contains the key and value
                    if (chld.getValue() != null) {
                        HashMap<String, Object> newPost = (HashMap<String, Object>) chld.getValue();
                        // Adding the data to the HashMap
                        hmap.put(chld.getKey(), newPost);
                        Log.d(TAG, "Hashmap key added " + chld.getKey().toString());
                        Log.d(TAG, "Hashmap value added " + newPost.toString());

                    } else {
                        Log.d(TAG, "Data Not Received");
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.getMessage());
                Log.e(TAG, firebaseError.getDetails());
            }
        });
        return hmap;
    }


    public void saveValue(String key, Map<String, Object> map) {
        Firebase ref = new Firebase(FIREBASE_DB);
        Firebase usersRef = ref.child(key);
        usersRef.updateChildren(map, new Firebase.CompletionListener() {
            //        usersRef.updateChildren({value, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.d(TAG, "Data could not be saved. " + firebaseError.getMessage());
                } else {
                    Log.d(TAG, "Data saved successfully.");
                }
            }
        });
    }

    public boolean isDataFetched()
    {
        return isDataChanged;
    }

    public String getValue(String key)
    {
        return fireBaseData.get(key);
    }

    public void fetchValue(String key) {

        Log.d(TAG, "Get Value for Key - " + key);
        Firebase ref = new Firebase(FIREBASE_DB + key);
        Query queryRef = ref.orderByKey();
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // snapshot contains the key and value
                isDataChanged = true;
                if(snapshot.getValue() != null)
                {
                    Log.d(TAG, "Data Received " + snapshot.getValue().toString());
                    // Adding the data to the HashMap
                    fireBaseData.put(snapshot.getKey(), snapshot.getValue().toString());
                }
                else {
                    Log.d(TAG, "Data Not Received");
                    fireBaseData.put(snapshot.getKey(), null);

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.getMessage());
                Log.e(TAG, firebaseError.getDetails());
            }
        });
    }
}
