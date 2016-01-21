package edu.neu.madcourse.binodthapachhetry;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.telephony.TelephonyManager;
import android.content.Context;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutActivityFragment extends Fragment {

//    private TelephonyManager telephonyManager;
//    private String deviceID;

    public AboutActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View aboutView = inflater.inflate(R.layout.fragment_about, container, false);

        String deviceID = getUniqueID();

        TextView name = (TextView) aboutView.findViewById(R.id.device_id);
        name.setText(deviceID);

        return aboutView;
    }

    public String getUniqueID(){
        String myAndroidDeviceId = "0000000000000000";
        TelephonyManager mTelephony = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (!mTelephony.getDeviceId().contains(myAndroidDeviceId)){
            myAndroidDeviceId = mTelephony.getDeviceId();}
        return myAndroidDeviceId;
    }
}
