package edu.neu.madcourse.binodthapachhetry;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.provider.Settings.Secure;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutActivityFragment extends Fragment {

    public AboutActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View aboutView = inflater.inflate(R.layout.fragment_about, container, false);

        String android_id = Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID);
        /* Get the widget with id name which is defined in the xml of the row */
        TextView name = (TextView) aboutView.findViewById(R.id.android_id);
        name.setText(android_id);

        return aboutView;
    }
}
